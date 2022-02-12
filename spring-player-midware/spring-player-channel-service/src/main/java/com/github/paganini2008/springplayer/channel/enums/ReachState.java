package com.github.paganini2008.springplayer.channel.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.paganini2008.devtools.enums.EnumConstant;
import com.github.paganini2008.devtools.enums.EnumUtils;

/**
 * 
 * ReachState
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public enum ReachState implements EnumConstant {

	WAITING(0, "待发送"), DELIVERED(1, "已发送"), FAILED(2, "已失败"), RECEIVED(3, "已接收");

	private ReachState(int value, String repr) {
		this.value = value;
		this.repr = repr;
	}

	private final int value;
	private final String repr;

	@Override
	@JsonValue
	public int getValue() {
		return value;
	}

	@Override
	public String getRepr() {
		return repr;
	}

	@JsonCreator
	public static ReachState valueOf(Integer type) {
		return EnumUtils.valueOf(ReachState.class, type);
	}

}
