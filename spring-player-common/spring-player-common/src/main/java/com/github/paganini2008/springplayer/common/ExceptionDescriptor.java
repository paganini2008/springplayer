package com.github.paganini2008.springplayer.common;

import org.springframework.http.HttpStatus;

/**
 * 
 * ExceptionDescriptor
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface ExceptionDescriptor {

	ErrorCode getErrorCode();
	
	default HttpStatus getHttpStatus() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
	
}
