package com.github.paganini2008.springplayer.common.gateway.route.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.paganini2008.springplayer.common.gateway.route.model.RouteConfig;
import com.github.paganini2008.springplayer.common.mybatis.EntityMapper;

/**
 * 
 * RouteConfigMapper
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Mapper
public interface RouteConfigMapper extends EntityMapper<RouteConfig> {

	List<String> getServiceIds(String env);
	
}
