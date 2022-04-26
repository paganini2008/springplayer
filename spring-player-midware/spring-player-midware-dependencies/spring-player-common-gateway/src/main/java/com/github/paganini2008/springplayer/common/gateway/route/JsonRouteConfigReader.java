package com.github.paganini2008.springplayer.common.gateway.route;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.paganini2008.springplayer.common.gateway.route.pojo.RouteConfigDTO;
import com.github.paganini2008.springplayer.common.utils.JacksonUtils;

/**
 * 
 * JsonRouteConfigReader
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class JsonRouteConfigReader implements RouteConfigReader {

	@Override
	public RouteConfigDTO[] load(File file) throws Exception {
		if (file == null) {
			return new RouteConfigDTO[0];
		}
		if (file.exists()) {
			String content = FileUtils.readFileToString(file, "utf-8");
			return load(content);
		}
		throw new FileNotFoundException(file.getAbsolutePath());
	}

	@Override
	public RouteConfigDTO[] load(String content) throws Exception {
		return StringUtils.isNotBlank(content) ? JacksonUtils.parseJson(content, RouteConfigDTO[].class) : new RouteConfigDTO[0];
	}

}
