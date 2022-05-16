package com.github.paganini2008.springplayer.common.gateway.route.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.paganini2008.springplayer.common.gateway.route.model.RouteFile;
import com.github.paganini2008.springplayer.common.mybatis.EntityMapper;

/**
 * 
 * RouteFileMapper
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Mapper
public interface RouteFileMapper extends EntityMapper<RouteFile> {
	
	List<String> getFileNames(String env);
	
}
