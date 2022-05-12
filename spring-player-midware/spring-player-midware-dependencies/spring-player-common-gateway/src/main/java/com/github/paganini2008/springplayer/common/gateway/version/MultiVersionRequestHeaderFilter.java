package com.github.paganini2008.springplayer.common.gateway.version;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;

import com.github.paganini2008.springplayer.common.gateway.GatewayConstants;

import reactor.core.publisher.Mono;

/**
 * 
 * MultiVersionRequestHeaderFilter
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@SuppressWarnings("all")
public class MultiVersionRequestHeaderFilter implements GlobalFilter {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private final PathMatcher pathMatcher = new AntPathMatcher();

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		if (exchange.getRequest().getHeaders().containsKey(GatewayConstants.REQUEST_HEADER_API_VERSION)) {
			return chain.filter(exchange);
		}
		Route route = (Route) exchange.getAttributes().get(GATEWAY_ROUTE_ATTR);
		List<Map<String, Object>> api = (List<Map<String, Object>>) route.getMetadata().get("api");
		if (CollectionUtils.isEmpty(api)) {
			return chain.filter(exchange);
		}
		String path = exchange.getRequest().getURI().getRawPath();
		String apiVersion = findVersion(api, path);
		if (StringUtils.isBlank(apiVersion)) {
			return chain.filter(exchange);
		}
		ServerHttpRequest newRequest = exchange.getRequest().mutate().header(GatewayConstants.REQUEST_HEADER_API_VERSION, apiVersion).build();
		return chain.filter(exchange.mutate().request(newRequest).build());
	}

	private String findVersion(List<Map<String, Object>> api, String requestPath) {
		for (Map<String, Object> m : api) {
			List<String> pathPatterns = (List<String>) m.get("path");
			for (String pathPattern : pathPatterns) {
				if (pathMatcher.match(pathPattern, requestPath)) {
					return (String) m.get("version");
				}
			}
		}
		return "";
	}

}
