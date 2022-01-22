package com.github.paganini2008.springplayer.common;

import org.springframework.http.HttpStatus;

/**
 * 
 * BizException
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class BizException extends RuntimeException implements ExceptionDescriptor {

	private static final long serialVersionUID = 1L;
	private final ErrorCode errorCode;
	private final HttpStatus httpStatus;

	public BizException(ErrorCode errorCode, HttpStatus httpStatus) {
		super("ERROR-" + errorCode.getMessageCode());
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
	}

	public BizException(ErrorCode errorCode, HttpStatus httpStatus, Throwable e) {
		super("ERROR-" + errorCode.getMessageCode(), e);
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	@Override
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
