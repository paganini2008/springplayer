package com.github.paganini2008.springplayer.common;

/**
 * 
 * ErrorCode
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface ErrorCode {
	
	String getMessageKey();
	
	String getMessageCode();

	default String getDefaultMessage() {
		return "";
	}
	
}
