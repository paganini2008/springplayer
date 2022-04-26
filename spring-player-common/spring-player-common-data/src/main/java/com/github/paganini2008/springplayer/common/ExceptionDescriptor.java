package com.github.paganini2008.springplayer.common;

import org.springframework.http.HttpStatus;

/**
 * 
 * ExceptionDescriptor
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public interface ExceptionDescriptor {

	static final String MESSAGE_FORMAT = "ERROR-%s: %s";

	String getMessage();

	ErrorCode getErrorCode();

	default HttpStatus getHttpStatus() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

}
