package com.github.paganini2008.springplayer.common.email;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.paganini2008.devtools.enums.EnumConstant;
import com.github.paganini2008.devtools.enums.EnumUtils;

/**
 * 
 * RichTextType
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public enum RichTextType implements EnumConstant {

	HTML(0, "HTML"), MARKDOWN(1, "Markdown"), FREEMARKER(2, "Freemarker"), THYMELEAF(3, "Thymeleaf");

	private final int value;
	private final String repr;

	private RichTextType(int value, String repr) {
		this.value = value;
		this.repr = repr;
	}

	@JsonValue
	@Override
	public int getValue() {
		return value;
	}

	@Override
	public String getRepr() {
		return repr;
	}

	@JsonCreator
	public static RichTextType valueOf(Integer type) {
		return EnumUtils.valueOf(RichTextType.class, type);
	}

}
