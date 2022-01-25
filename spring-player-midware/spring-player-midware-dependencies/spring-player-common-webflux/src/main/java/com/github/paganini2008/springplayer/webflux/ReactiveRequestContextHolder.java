package com.github.paganini2008.springplayer.webflux;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

/**
 * 
 * ReactiveRequestContextHolder
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public class ReactiveRequestContextHolder implements WebFilter {
	
	static final String REQUEST_CONTEXT_KEY = ServerHttpRequest.class.getName();

	public static Mono<ServerHttpRequest> getRequest() {
		return Mono.subscriberContext().map(ctx -> ctx.get(REQUEST_CONTEXT_KEY));
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		return chain.filter(exchange).subscriberContext(ctx -> ctx.put(chain, request));
	}

}
