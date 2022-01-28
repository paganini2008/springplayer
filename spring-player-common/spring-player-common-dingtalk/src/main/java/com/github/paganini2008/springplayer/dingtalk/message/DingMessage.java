package com.github.paganini2008.springplayer.dingtalk.message;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * DingMessage
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public abstract class DingMessage {

	private String serviceUrl;
	private String template;
	private Map<String, Object> variables;
	private String[] mobiles;
	
}
