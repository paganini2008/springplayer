package com.github.paganini2008.springplayer.webflux;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.github.paganini2008.springplayer.common.Constants;

import reactor.core.publisher.Mono;

/**
 * 
 * RequestHeaderContextHolder
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public class RequestHeaderContextHolder implements WebFilter {

	private static final ThreadLocal<Map<String, String>> headers = TransmittableThreadLocal.withInitial(() -> {
		return new ConcurrentHashMap<>();
	});

	public static String getHeader(String headerName) {
		return getHeader(headerName, "");
	}

	public static String getHeader(String headerName, String defaultValue) {
		return headers.get().getOrDefault(headerName, defaultValue);
	}

	public static void setHeader(String headerName, String headerValue) {
		headers.get().put(headerName, headerValue);
	}

	public static Long getTenantId() {
		return Long.valueOf(getHeader(Constants.TENANT_ID, "1"));
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		Map<String, String> requestHeaders = exchange.getRequest().getHeaders().toSingleValueMap().entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedCaseInsensitiveMap::new));
		headers.set(requestHeaders);
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			headers.remove();
		}));
	}

}
