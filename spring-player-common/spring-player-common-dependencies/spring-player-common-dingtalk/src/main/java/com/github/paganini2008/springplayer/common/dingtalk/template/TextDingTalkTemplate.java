package com.github.paganini2008.springplayer.common.dingtalk.template;

import java.util.Map;

import com.github.paganini2008.devtools.StringUtils;

/**
 * 
 * TextDingTalkTemplate
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class TextDingTalkTemplate implements DingTalkTemplate{

	@Override
	public String loadContent(String templateName, String templateContent, Map<String, Object> kwargs) {
		return StringUtils.parseText(templateContent, "${", "}", kwargs);
	}

}
