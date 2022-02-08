package com.github.paganini2008.springplayer.gateway.route.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paganini2008.springplayer.gateway.route.mapper.RouteConfigMapper;
import com.github.paganini2008.springplayer.gateway.route.model.RouteConfig;

/**
 * 
 * RouteConfigServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class RouteConfigServiceImpl extends ServiceImpl<RouteConfigMapper, RouteConfig> implements RouteConfigService {

	@Autowired
	private RouteConfigMapper routeConfigMapper;

	@Override
	public List<String> getServiceIds(String env) {
		return routeConfigMapper.getServiceIds(env);
	}

}
