package com.github.paganini2008.springplayer.messenger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.paganini2008.devtools.enums.EnumConstant;
import com.github.paganini2008.devtools.enums.EnumUtils;

/**
 * 
 * MessageType
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public enum MessageType implements EnumConstant {
	
	DING_TALK(0, "钉钉"), SMS(1, "短信"), EMAIL(2, "邮件"), WECHAT(3, "微信");

	private MessageType(int value, String repr) {
		this.value = value;
		this.repr = repr;
	}

	private final int value;
	private final String repr;

	@JsonValue
	public int getValue() {
		return value;
	}

	@Override
	public String getRepr() {
		return repr;
	}

	@JsonCreator
	public static MessageType valueOf(Integer type) {
		return EnumUtils.valueOf(MessageType.class, type);
	}

}
