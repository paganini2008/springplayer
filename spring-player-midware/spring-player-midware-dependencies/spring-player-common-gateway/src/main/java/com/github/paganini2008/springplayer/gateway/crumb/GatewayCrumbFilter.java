package com.github.paganini2008.springplayer.gateway.crumb;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_PARENT_SPAN_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_SPAN_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACES;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE_ID;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.collection.CollectionUtils;
import com.github.paganini2008.springplayer.common.JacksonUtils;
import com.github.paganini2008.springplayer.common.monitor.RequestConcurrencyContextHolder;
import com.github.paganini2008.springplayer.common.monitor.crumb.Span;
import com.github.paganini2008.springplayer.common.monitor.crumb.SpanTree;
import com.github.paganini2008.springplayer.common.monitor.crumb.TraceStore;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * 
 * GatewayCrumbFilter
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class GatewayCrumbFilter implements GlobalFilter {

	private static final String HTTP_TRACE_SPAN = "httpTraceSpan";

	private final RedisAtomicLong spanGen;
	private final TraceStore traceStore;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String traceId = request.getId();
		if (StringUtils.isBlank(traceId)) {
			traceId = UUID.randomUUID().toString();
		}
		long timestamp = System.currentTimeMillis();
		String timestampStr = request.getHeaders().getFirst(REQUEST_HEADER_TIMESTAMP);
		if (StringUtils.isNotBlank(timestampStr)) {
			timestamp = Long.parseLong(timestampStr);
		}
		Span span = new Span();
		span.setTraceId(traceId);
		Set<URI> uris = exchange.getRequiredAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
		String path = CollectionUtils.isNotEmpty(uris) ? CollectionUtils.getFirst(uris).getRawPath()
				: request.getPath().pathWithinApplication().value();
		span.setPath(path);
		span.setTimestamp(timestamp);
		span.setSpanId(spanGen.getAndIncrement());
		span.setParentSpanId(0);

		exchange.getAttributes().put(HTTP_TRACE_SPAN, span);

		RequestConcurrencyContextHolder.get(span.getPath()).increment();
		ServerHttpRequest newRequest = request.mutate().header(REQUEST_HEADER_TRACE_ID, span.getTraceId())
				.header(REQUEST_HEADER_PARENT_SPAN_ID, String.valueOf(span.getSpanId()))
				.header(REQUEST_HEADER_TIMESTAMP, String.valueOf(span.getTimestamp())).build();

		return chain.filter(exchange.mutate().request(newRequest).build()).then(Mono.fromRunnable(() -> {
			Span payload = (Span) exchange.getAttribute(HTTP_TRACE_SPAN);
			if (payload != null) {
				RequestConcurrencyContextHolder.get(payload.getPath()).decrement();

				ServerHttpResponse response = exchange.getResponse();
				payload.setStatus(response.getRawStatusCode());
				payload.setElapsed(System.currentTimeMillis() - payload.getTimestamp());
				payload.setConcurrents(RequestConcurrencyContextHolder.get(payload.getPath()).get());

				List<String> traces = response.getHeaders().remove(REQUEST_HEADER_TRACES);
				if (CollectionUtils.isNotEmpty(traces) && StringUtils.isNotBlank(traces.get(0))) {
					String trace = traces.get(0);
					List<Span> spans = StringUtils.split(trace, ";").stream().map(str -> JacksonUtils.parseJson(str, Span.class))
							.collect(Collectors.toList());
					spans.add(payload);
					SpanTree spanTree = SpanTree.load(spans);
					if (spanTree != null && traceStore.shouldTrace(payload)) {
						traceStore.trace(spanTree);
					}
				}

				response.getHeaders().remove(REQUEST_HEADER_TRACE_ID);
				response.getHeaders().remove(REQUEST_HEADER_PARENT_SPAN_ID);
				response.getHeaders().remove(REQUEST_HEADER_SPAN_ID);
				response.getHeaders().remove(REQUEST_HEADER_TIMESTAMP);
			}

		}));
	}

}
