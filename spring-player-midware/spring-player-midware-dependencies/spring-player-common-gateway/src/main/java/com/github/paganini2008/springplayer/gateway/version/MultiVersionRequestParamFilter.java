package com.github.paganini2008.springplayer.gateway.version;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import com.github.paganini2008.springplayer.gateway.GatewayConstants;

import reactor.core.publisher.Mono;

/**
 * 
 * 支持多版本API过滤请求参数: API_VERSION
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class MultiVersionRequestParamFilter implements GlobalFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest req = exchange.getRequest();
		String queryString = req.getURI().getQuery();
		if (StringUtils.isBlank(queryString)) {
			return chain.filter(exchange);
		}
		Map<String, String> queryMap = getParameterMap(queryString);
		if (!queryMap.containsKey(GatewayConstants.REQUEST_HEADER_API_VERSION)) {
			return chain.filter(exchange);
		}
		String apiVersion = queryMap.get(GatewayConstants.REQUEST_HEADER_API_VERSION);
		if (StringUtils.isNotBlank(apiVersion)) {
			ServerHttpRequest newRequest = exchange.getRequest().mutate().header(GatewayConstants.REQUEST_HEADER_API_VERSION, apiVersion)
					.build();
			return chain.filter(exchange.mutate().request(newRequest).build());
		}
		return chain.filter(exchange);
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getParameterMap(String queryString) {
		Map<String, String> map = Arrays.stream(queryString.split("&")).map(s -> s.split("=", 2))
				.collect(Collectors.toMap(args -> args[0], args -> args[1]));
		return new CaseInsensitiveMap(map);
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 200;
	}

}
