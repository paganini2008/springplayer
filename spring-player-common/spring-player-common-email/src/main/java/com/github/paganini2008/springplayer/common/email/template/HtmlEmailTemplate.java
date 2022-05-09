package com.github.paganini2008.springplayer.common.email.template;

import java.util.Map;

import com.github.paganini2008.devtools.StringUtils;

/**
 * 
 * HtmlEmailTemplate
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class HtmlEmailTemplate implements EmailTemplate {

	@Override
	public String loadContent(String templateName, String templateContent, Map<String, Object> kwargs) throws Exception {
		return StringUtils.parseText(templateContent, "${", "}", kwargs);
	}

}
