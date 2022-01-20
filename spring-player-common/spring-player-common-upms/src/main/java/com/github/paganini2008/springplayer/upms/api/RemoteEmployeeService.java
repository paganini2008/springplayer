package com.github.paganini2008.springplayer.upms.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.feign.SpFeignConfig;
import com.github.paganini2008.springplayer.upms.model.Employee;

/**
 * 
 * RemoteEmployeeService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@FeignClient(contextId = "remoteEmployeeService", value = "sp-upms-service", configuration = SpFeignConfig.class)
public interface RemoteEmployeeService {

	@PostMapping("/emp/find/{username}")
	ApiResult<Employee> findEmployeeByName(@PathVariable String username);

}
