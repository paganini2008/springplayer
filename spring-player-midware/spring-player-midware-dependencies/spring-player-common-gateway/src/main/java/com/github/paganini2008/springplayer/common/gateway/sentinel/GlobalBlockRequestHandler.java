package com.github.paganini2008.springplayer.common.gateway.sentinel;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.github.paganini2008.devtools.ExceptionUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.webflux.ApiExceptionContext;
import com.github.paganini2008.springplayer.common.webflux.ThrowableInfo;

import reactor.core.publisher.Mono;

/**
 * 
 * GlobalBlockRequestHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class GlobalBlockRequestHandler implements BlockRequestHandler {

	@Autowired(required = false)
	private ApiExceptionContext context;

	@Override
	public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable e) {
		ServerHttpRequest httpRequest = exchange.getRequest();
		String path = httpRequest.getPath().pathWithinApplication().value();
		if (e != null && context != null) {
			context.getExceptionTraces().add(new ThrowableInfo(path, e.getMessage(), ExceptionUtils.toArray(e), LocalDateTime.now()));
		}
		ApiResult<String> result = ApiResult.failed("接口已限流");
		result.setRequestPath(path);
		String timestamp = httpRequest.getHeaders().getFirst("timestamp");
		if (StringUtils.isNotBlank(timestamp)) {
			try {
				result.setElapsed(System.currentTimeMillis() - Long.parseLong(timestamp));
			} catch (RuntimeException ignored) {
			}
		}
		return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(result));
	}

}
