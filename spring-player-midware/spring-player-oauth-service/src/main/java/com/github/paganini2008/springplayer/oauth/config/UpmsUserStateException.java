package com.github.paganini2008.springplayer.oauth.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountStatusException;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.paganini2008.springplayer.common.ErrorCode;
import com.github.paganini2008.springplayer.common.ExceptionDescriptor;
import com.github.paganini2008.springplayer.webmvc.JacksonExceptionSerializer;

/**
 * 
 * UserStateException
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@JsonSerialize(using = JacksonExceptionSerializer.class)
public class UpmsUserStateException extends AccountStatusException implements ExceptionDescriptor {

	private static final long serialVersionUID = 4799126863811215723L;

	public UpmsUserStateException(ErrorCode errorCode) {
		super("ERROR-" + errorCode.getMessageCode());
		this.errorCode = errorCode;
	}

	private final ErrorCode errorCode;

	@Override
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.UNAUTHORIZED;
	}

}
