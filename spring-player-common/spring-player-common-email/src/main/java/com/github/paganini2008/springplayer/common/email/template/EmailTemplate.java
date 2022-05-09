package com.github.paganini2008.springplayer.common.email.template;

import java.util.Map;

/**
 * 
 * EmailTemplate
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface EmailTemplate {

	String loadContent(String templateName, String templateContent, Map<String, Object> kwargs) throws Exception;

	default boolean isHtml() {
		return true;
	}

}
