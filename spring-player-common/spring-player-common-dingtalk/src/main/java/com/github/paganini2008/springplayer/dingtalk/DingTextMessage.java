package com.github.paganini2008.springplayer.dingtalk;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * DingTextMessage
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class DingTextMessage {

	private String serviceUrl;
	private String title;
	private String template;
	private Map<String, Object> variables;
	private String[] mobiles;
	
}
