package com.github.paganini2008.springplayer.common.gateway.route;

import java.util.Optional;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.gateway.route.pojo.RouteConfigDTO;
import com.github.paganini2008.springplayer.common.gateway.route.pojo.RouteConfigDTO.NameArgs;

/**
 * 
 * RouteConfigUtils
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class RouteConfigUtils {

	public static String extractPath(RouteConfigDTO dto) {
		Optional<NameArgs> op = dto.getPredicates().stream()
				.filter(p -> StringUtils.isNotBlank(p.getText()) && p.getText().startsWith("Path")).findFirst();
		if (op.isPresent()) {
			String text = op.get().getText();
			return text.substring(text.indexOf("=") + 1);
		}
		return "";
	}

}
