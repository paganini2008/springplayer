package com.github.paganini2008.springplayer.gateway.route.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paganini2008.springplayer.gateway.route.mapper.RouteFileMapper;
import com.github.paganini2008.springplayer.gateway.route.model.RouteFile;

/**
 * 
 * RouteFileServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class RouteFileServiceImpl extends ServiceImpl<RouteFileMapper, RouteFile> implements RouteFileService {

	@Autowired
	private RouteFileMapper routeFileMapper;

	@Override
	public List<String> getFileNames(String env) {
		return routeFileMapper.getFileNames(env);
	}

}
