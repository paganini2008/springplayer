package com.github.paganini2008.springplayer.i18n.api;

import org.springframework.cloud.openfeign.FeignClient;

import com.github.paganini2008.springplayer.feign.SpFeignConfig;

/**
 * 
 * RemoteMessageService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@FeignClient(contextId = "remoteMessageService", value = "sp-i18n-service", configuration = SpFeignConfig.class)
public interface RemoteMessageService {

}
