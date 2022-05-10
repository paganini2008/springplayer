package com.github.paganini2008.springplayer.common.context;

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
		return getMessage(errorCode.getMessageKey(), locale, null, errorCode.getDefaultMessage());
	}

	String getMessage(String code, Locale locale, Object[] args, String defaultMessage);

}
