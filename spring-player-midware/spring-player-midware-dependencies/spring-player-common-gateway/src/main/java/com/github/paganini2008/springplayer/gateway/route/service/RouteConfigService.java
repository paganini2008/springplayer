package com.github.paganini2008.springplayer.gateway.route.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paganini2008.springplayer.gateway.route.model.RouteConfig;

/**
 * 
 * RouteConfigService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface RouteConfigService extends IService<RouteConfig> {

	List<String> getServiceIds(String env);
	
}
