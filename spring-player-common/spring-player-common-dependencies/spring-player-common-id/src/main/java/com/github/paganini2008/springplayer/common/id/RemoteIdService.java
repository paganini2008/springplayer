package com.github.paganini2008.springplayer.common.id;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.paganini2008.springplayer.common.ApiResult;

/**
 * 
 * RemoteIdService
 *
 * @author Fred Feng
 *
 * @version 2.0.5
 */
@FeignClient(contextId = "remoteIdService", name = "spring-player-id-service")
public interface RemoteIdService {

	@GetMapping("/id/next")
	ApiResult<Long> getNextValue();

	@GetMapping("/id/current")
	ApiResult<Long> getCurrentValue();

}
