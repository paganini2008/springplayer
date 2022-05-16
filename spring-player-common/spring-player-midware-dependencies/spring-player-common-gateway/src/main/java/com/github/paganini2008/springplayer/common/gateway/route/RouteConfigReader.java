package com.github.paganini2008.springplayer.common.gateway.route;

import java.io.File;

import com.github.paganini2008.springplayer.common.gateway.route.pojo.RouteConfigDTO;

/**
 * 
 * RouteConfigReader
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface RouteConfigReader {

	/**
	 * 解析文件获取路由配置
	 * 
	 * @param file 系统文件
	 * @return
	 * @throws Exception
	 */
	RouteConfigDTO[] load(File file) throws Exception;

	/**
	 * 解析文本获取路由配置
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	RouteConfigDTO[] load(String text) throws Exception;

}