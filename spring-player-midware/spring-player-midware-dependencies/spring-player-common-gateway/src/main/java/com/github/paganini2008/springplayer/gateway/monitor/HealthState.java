package com.github.paganini2008.springplayer.gateway.monitor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.paganini2008.devtools.enums.EnumConstant;
import com.github.paganini2008.devtools.enums.EnumUtils;

/**
 * 
 * HealthState
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public enum HealthState implements EnumConstant {

	HEALTHY(0, "健康"), SUB_HEALTHY(1, "亚健康"), UNHEALTHY(2, "不健康"), FATAL(3, "严重的");

	private final int value;
	private final String repr;

	private HealthState(int value, String repr) {
		this.value = value;
		this.repr = repr;
	}

	@JsonValue
	public int getValue() {
		return value;
	}

	@Override
	public String getRepr() {
		return repr;
	}

	@JsonCreator
	public static HealthState valueOf(Integer type) {
		return EnumUtils.valueOf(HealthState.class, type);
	}

}
