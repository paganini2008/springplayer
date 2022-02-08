package com.github.paganini2008.springplayer.gateway.trace;

import static com.github.paganini2008.springplayer.common.Constants.*;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_SPAN;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE_ID;

import java.util.UUID;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.github.paganini2008.devtools.StringUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * 
 * TraceFilter
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class TraceFilter implements GlobalFilter {

	private final RedisAtomicLong spanGen;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String traceId = request.getId();
		if (StringUtils.isBlank(traceId)) {
			traceId = UUID.randomUUID().toString();
		}
		String timestamp = request.getHeaders().getFirst(REQUEST_HEADER_TIMESTAMP);
		if (StringUtils.isBlank(timestamp)) {
			timestamp = String.valueOf(System.currentTimeMillis());
		}
		ServerHttpRequest newRequest = request.mutate().header(REQUEST_HEADER_TRACE_ID, traceId)
				.header(REQUEST_HEADER_SPAN, String.valueOf(spanGen.getAndIncrement())).header(REQUEST_HEADER_PARENT_SPAN, "0")
				.header(REQUEST_HEADER_TIMESTAMP, timestamp).build();
		return chain.filter(exchange.mutate().request(newRequest).build()).then(Mono.fromRunnable(() -> {
			ServerHttpResponse response = exchange.getResponse();
			String trace = response.getHeaders().getFirst(REQUEST_HEADER_TRACE);
		}));
	}

}
