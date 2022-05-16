package com.github.paganini2008.springplayer.common;

/**
 * 
 * SimpleErrorCode
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class SimpleErrorCode implements ErrorCode {

	private final String messageKey;
	private final String messageCode;
	private final String defaultMessage;

	public SimpleErrorCode(String messageKey, String messageCode) {
		this(messageKey, messageCode, "");
	}

	public SimpleErrorCode(String messageKey, String messageCode, String defaultMessage) {
		this.messageKey = messageKey;
		this.messageCode = messageCode;
		this.defaultMessage = defaultMessage;
	}

	@Override
	public String getMessageKey() {
		return messageKey;
	}

	@Override
	public String getMessageCode() {
		return messageCode;
	}

	@Override
	public String getDefaultMessage() {
		return defaultMessage;
	}

}
