package com.github.paganini2008.springplayer.upms;

import org.springframework.http.HttpStatus;

import com.github.paganini2008.springplayer.common.BizException;
import com.github.paganini2008.springplayer.common.ErrorCode;

/**
 * 
 * UpmsException
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class UpmsException extends BizException {

	private static final long serialVersionUID = 7402278225708723033L;

	public UpmsException(ErrorCode errorCode) {
		this(errorCode, HttpStatus.UNAUTHORIZED);
	}

	public UpmsException(ErrorCode errorCode, HttpStatus httpStatus) {
		super(errorCode, httpStatus);
	}

	public UpmsException(ErrorCode errorCode, HttpStatus httpStatus, Throwable e) {
		super(errorCode, httpStatus, e);
	}

}
