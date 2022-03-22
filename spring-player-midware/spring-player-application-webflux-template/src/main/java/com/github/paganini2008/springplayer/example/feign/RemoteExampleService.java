package com.github.paganini2008.springplayer.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.paganini2008.springplayer.common.ApiResult;

/**
 * 
 * RemoteExampleService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@FeignClient(contextId = "remoteExampleService", name = "example-service")
public interface RemoteExampleService {

	@GetMapping("/echo")
	ApiResult<String> echo(@RequestParam("q") String q);

}
