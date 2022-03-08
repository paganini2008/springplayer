package com.yl.platform.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.paganini2008.springplayer.common.ApiResult;

/**
 * 
 * RemoteTestService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@FeignClient(contextId = "remoteTestService", name = "test-service")
public interface RemoteTestService {

	@GetMapping("/echo")
	ApiResult<String> echo(@RequestParam("q") String q);

}
