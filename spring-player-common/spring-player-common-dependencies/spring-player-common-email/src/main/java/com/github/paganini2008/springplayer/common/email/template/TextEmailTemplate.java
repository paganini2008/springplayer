package com.github.paganini2008.springplayer.common.email.template;

import java.util.Map;

import com.github.paganini2008.devtools.StringUtils;

/**
 * 
 * TextEmailTemplate
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class TextEmailTemplate implements EmailTemplate {

	@Override
	public String loadContent(String templateName, String templateContent, Map<String, Object> kwargs) throws Exception {
		return StringUtils.parseText(templateContent, "${", "}", kwargs);
	}

	@Override
	public boolean isHtml() {
		return false;
	}

}
