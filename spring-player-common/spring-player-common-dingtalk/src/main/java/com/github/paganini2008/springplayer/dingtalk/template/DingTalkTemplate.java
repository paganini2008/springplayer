package com.github.paganini2008.springplayer.dingtalk.template;

import java.util.Map;

/**
 * 
 * DingTalkTemplate
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface DingTalkTemplate {

	String loadContent(String templateName, String templateContent, Map<String, Object> kwargs);

}
