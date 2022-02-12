package com.github.paganini2008.springplayer.channel.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.paganini2008.devtools.enums.EnumConstant;
import com.github.paganini2008.devtools.enums.EnumUtils;

/**
 * 
 * ChannelType
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public enum ChannelType implements EnumConstant {
	
	DING_TALK(0, "钉钉"), SMS(1, "短信"), EMAIL(2, "邮件"), WECHAT(3, "微信");

	private ChannelType(int value, String repr) {
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
	public static ChannelType valueOf(Integer type) {
		return EnumUtils.valueOf(ChannelType.class, type);
	}

}
