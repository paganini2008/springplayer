package com.github.paganini2008.springplayer.upms;

import org.springframework.http.HttpStatus;

import com.github.paganini2008.springplayer.common.BizException;
import com.github.paganini2008.springplayer.common.ErrorCode;

/**
 * 
 * SpUpmsException
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class SpUpmsException extends BizException {

	private static final long serialVersionUID = 7402278225708723033L;

	public SpUpmsException(ErrorCode errorCode) {
		this(errorCode, HttpStatus.UNAUTHORIZED);
	}

	public SpUpmsException(ErrorCode errorCode, HttpStatus httpStatus) {
		super(errorCode, httpStatus);
	}

	public SpUpmsException(ErrorCode errorCode, HttpStatus httpStatus, Throwable e) {
		super(errorCode, httpStatus, e);
	}

}
