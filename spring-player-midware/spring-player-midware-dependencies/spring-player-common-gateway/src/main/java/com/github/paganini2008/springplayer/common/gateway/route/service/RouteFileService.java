package com.github.paganini2008.springplayer.common.gateway.route.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paganini2008.springplayer.common.gateway.route.model.RouteFile;

/**
 * 
 * RouteFileService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface RouteFileService extends IService<RouteFile> {

	List<String> getFileNames(String env);
	
}
