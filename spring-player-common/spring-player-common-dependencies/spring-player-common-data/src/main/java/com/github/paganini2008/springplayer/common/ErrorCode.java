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

	static ErrorCode feignClientError(String msg) {
		return new SimpleErrorCode("FEIGN_CLIENT_ERROR", "9999998", msg);
	}

	static ErrorCode internalServerError(String msg) {
		return new SimpleErrorCode("INTERNAL_SERVER_ERROR", "9999999", msg);
	}

}
