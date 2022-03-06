package com.github.paganini2008.springplayer.redis.pubsub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.paganini2008.devtools.enums.EnumConstant;
import com.github.paganini2008.devtools.enums.EnumUtils;

/**
 * 
 * PubSubMode
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public enum PubSubMode implements EnumConstant {

	UNICAST(0, "unicast"), MULTICAST(1, "multicast");

	private final int value;
	private final String repr;

	private PubSubMode(int value, String repr) {
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
	public static PubSubMode valueOf(Integer type) {
		return EnumUtils.valueOf(PubSubMode.class, type);
	}

}
