package com.github.paganini2008.springplayer.common.webmvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.ApiResult;

/**
 * 
 * WebMvcEndpoint
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RestController
public class WebMvcEndpoint {

	@Autowired
	private ApiExceptionContext exceptionContext;

	@GetMapping("/ping")
	public ApiResult<String> ping(@RequestParam(name = "q", required = false) String q) {
		return ApiResult.ok(StringUtils.isNotBlank(q) ? "pong: " + q : "pong");
	}

	@GetMapping("/sys/latest/throwables")
	public ApiResult<List<ThrowableInfo>> getLatestThrowables() {
		List<ThrowableInfo> list = new ArrayList<>(exceptionContext.getExceptionTraces());
		Collections.sort(list);
		return ApiResult.ok(list);
	}

}
