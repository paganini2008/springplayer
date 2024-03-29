package com.github.paganini2008.springplayer.common.upms.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.upms.vo.UserVO;

/**
 * 
 * RemoteUserService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@FeignClient(contextId = "remoteUserService", value = "spring-player-upms-service")
public interface RemoteUserService {
	
	@PostMapping("/user/info/{username}")
	ApiResult<UserVO> getUserInfo(@PathVariable("username") String username);

}
