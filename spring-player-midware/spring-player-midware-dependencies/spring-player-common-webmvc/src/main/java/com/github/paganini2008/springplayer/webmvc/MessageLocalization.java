package com.github.paganini2008.springplayer.webmvc;

import java.util.Locale;

import com.github.paganini2008.springplayer.common.ErrorCode;

/**
 * 
 * MessageLocalization
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface MessageLocalization {

	default String getMessage(ErrorCode errorCode, Locale locale) {
		return getMessage(errorCode.getMessageKey(), null, errorCode.getDefaultMessage(), locale);
	}

	String getMessage(String code, Object[] args, String defaultMessage, Locale locale);

}
