package com.github.paganini2008.springplayer.common;

import org.springframework.http.HttpStatus;

/**
 * 
 * OpenApiException
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class OpenApiException extends BizException {

	private static final long serialVersionUID = 3123139368923934917L;

	public OpenApiException(ErrorCode errorCode, HttpStatus httpStatus) {
		super(errorCode, httpStatus);
	}

	public OpenApiException(ErrorCode errorCode, HttpStatus httpStatus, Throwable e) {
		super(errorCode, httpStatus, e);
	}

}
