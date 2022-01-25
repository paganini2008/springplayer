package com.github.paganini2008.springplayer.webflux;

import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TIMESTAMP;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

/**
 * 
 * BasicFilter
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "springplayer.common.webflux")
public class BasicFilter implements WebFilter {

	@Getter
	@Setter
	private List<String> defaultHeaders = new ArrayList<>();

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest newRequest = exchange.getRequest().mutate().headers(headerMap -> {
			if (!headerMap.containsKey(REQUEST_HEADER_TIMESTAMP)) {
				headerMap.set(REQUEST_HEADER_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
			}

			defaultHeaders.forEach(line -> {
				int index = line.indexOf("=");
				if (index > 0) {
					String headerName = line.substring(0, index);
					String headerValue = line.substring(index + 1);
					headerMap.add(headerName, headerValue);
				}
			});
		}).build();
		return chain.filter(exchange.mutate().request(newRequest).build());
	}

}
