package com.github.paganini2008.springplayer.gateway.monitor;

import static com.github.paganini2008.springplayer.gateway.GatewayConstants.HTTP_REQUEST_TRACE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import com.github.paganini2008.devtools.collection.MapUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 
 * ApiWatcherFilter
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@Slf4j
public class ApiWatcherFilter implements GlobalFilter {

	@Autowired
	private ApiWatcherContext context;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		HttpRequestTrace requestTrace = (HttpRequestTrace) exchange.getAttributes().get(HTTP_REQUEST_TRACE);
		if (requestTrace == null) {
			exchange.getAttributes().putIfAbsent(HTTP_REQUEST_TRACE, HttpTraceUtils.newTrace(exchange));
			requestTrace = (HttpRequestTrace) exchange.getAttributes().get(HTTP_REQUEST_TRACE);
		}
		context.incrementConcurrents(requestTrace);

		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			HttpRequestTrace payload = exchange.getAttribute(HTTP_REQUEST_TRACE);
			if (payload != null) {
				context.decrementConcurrents(payload);
				ServerHttpResponse httpResponse = exchange.getResponse();
				writeStatistics(payload, httpResponse);
			}
		}));
	}

	private void writeStatistics(HttpRequestTrace payload, ServerHttpResponse httpResponse) {
		long startTime = payload.getTimestamp();
		HttpResponseTrace trace = new HttpResponseTrace(payload, httpResponse.getStatusCode(), context.getConcurrents(payload));
		MultiValueMap<String, ?> map = httpResponse.getHeaders();
		if (MapUtils.isNotEmpty(map)) {
			trace.setHeader(map.toSingleValueMap());
		}
		map = httpResponse.getCookies();
		if (MapUtils.isNotEmpty(map)) {
			trace.setCookie(map.toSingleValueMap());
		}

		trace.printPrettyLog(log);
		try {
			context.record(trace);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if (log.isTraceEnabled()) {
			log.trace("[ApiStatistics] Payload '{}' take: {} ms", payload.getPath(), (System.currentTimeMillis() - startTime));
		}
	}

}
