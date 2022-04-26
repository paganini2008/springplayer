package com.github.paganini2008.springplayer.common.sentinel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.paganini2008.devtools.enums.EnumConstant;
import com.github.paganini2008.devtools.enums.EnumUtils;

/**
 * 
 * RuleType
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public enum RuleType implements EnumConstant {

	AUTHORITY(0, "授权规则"),

	DEGRADE(1, "降级规则"), 
	
	SYSTEM(2, "系统规则"),

	FLOW(3, "流控规则"),

	PARAM_FLOW(4, "热点参数规则"),
	
	GATEWAY_FLOW(5, "网关流控规则");

	private RuleType(int value, String repr) {
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
	public static RuleType valueOf(Integer type) {
		return EnumUtils.valueOf(RuleType.class, type);
	}

}
