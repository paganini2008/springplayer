package com.github.paganini2008.springplayer.messenger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.paganini2008.devtools.enums.EnumConstant;
import com.github.paganini2008.devtools.enums.EnumUtils;

/**
 * 
 * DingMessageType
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public enum DingMessageType implements EnumConstant {

	TEXT(0, "Text"), LINK(1, "Link"), MARKDOWN(2, "Markdown"), ACTION_CARD(3, "actionCard"), FEED_CARD(4, "feedCard");

	private DingMessageType(int value, String repr) {
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
	public static DingMessageType valueOf(Integer type) {
		return EnumUtils.valueOf(DingMessageType.class, type);
	}

}
