package com.github.paganini2008.springplayer.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.paganini2008.springplayer.common.JacksonExceptionSerializer;
import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.common.ExceptionDescriptor;

/**
 * 
 * OpenApiException
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@JsonSerialize(using = JacksonExceptionSerializer.class)
public class OpenApiException extends OAuth2Exception implements ExceptionDescriptor {

	private static final long serialVersionUID = 3123139368923934917L;

	public OpenApiException(ErrorCode errorCode) {
		this("ERROR-" + errorCode.getMessageCode(), errorCode);
	}

	public OpenApiException(String msg, ErrorCode errorCode) {
		super(msg);
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
