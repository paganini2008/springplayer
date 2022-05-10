package com.github.paganini2008.springplayer.common.upms.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.upms.vo.EmployeeVO;

/**
 * 
 * RemoteEmployeeService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@FeignClient(contextId = "remoteEmployeeService", value = "spring-player-upms-service")
public interface RemoteEmployeeService {

	@PostMapping("/emp/find/{username}")
	ApiResult<EmployeeVO> findEmployeeByName(@PathVariable String username);

}
