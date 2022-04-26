package com.github.paganini2008.springplayer.oauth.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.common.ExceptionDescriptor;
import com.github.paganini2008.springplayer.common.webmvc.JacksonExceptionSerializer;

/**
 * 
 * OAuth2ServerException
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@JsonSerialize(using = JacksonExceptionSerializer.class)
public class OAuth2ServerException extends OAuth2Exception implements ExceptionDescriptor {

	private static final long serialVersionUID = 3123139368923934917L;

	public OAuth2ServerException(ErrorCode errorCode) {
		super(String.format(MESSAGE_FORMAT, errorCode.getMessageCode(), errorCode.getMessageKey()));
		this.errorCode = errorCode;
	}

	public OAuth2ServerException(ErrorCode errorCode, Throwable e) {
		super(String.format(MESSAGE_FORMAT, errorCode.getMessageCode(), errorCode.getMessageKey()), e);
		this.errorCode = errorCode;
	}

	private final ErrorCode errorCode;

	@Override
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.valueOf(getHttpErrorCode());
	}

}
