package com.github.paganini2008.springplayer.gateway.monitor;

import static com.github.paganini2008.springplayer.gateway.GatewayConstants.CACHE_REQUEST_BODY_OBJECT_KEY;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

/**
 * 
 * HttpTraceUtils
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class HttpTraceUtils {

	public static HttpRequestTrace newTrace(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		HttpRequestTrace requestTrace = HttpRequestTrace.extractFrom(request);
		requestTrace.setBody((String) exchange.getAttribute(CACHE_REQUEST_BODY_OBJECT_KEY));
		return requestTrace;
	}

}
