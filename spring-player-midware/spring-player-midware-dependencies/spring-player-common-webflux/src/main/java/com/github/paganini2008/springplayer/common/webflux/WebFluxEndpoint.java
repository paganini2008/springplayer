package com.github.paganini2008.springplayer.common.webflux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;

import reactor.core.publisher.Mono;

/**
 * 
 * WebFluxEndpoint
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RestController
public class WebFluxEndpoint {
	
	@Autowired
	private ApiExceptionContext exceptionContext;

	@GetMapping("/ping")
	public Mono<ApiResult<String>> ping(@RequestParam(name = "q", required = false) String q) {
		return Mono.just(ApiResult.ok(StringUtils.isNotBlank(q) ? "pong: " + q : "pong"));
	}
	
	@GetMapping("/latest/throwables")
	public ApiResult<List<ThrowableInfo>> getLatestThrowables() {
		List<ThrowableInfo> list = new ArrayList<>(exceptionContext.getExceptionTraces());
		Collections.sort(list);
		return ApiResult.ok(list);
	}
}
