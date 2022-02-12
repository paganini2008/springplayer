package com.github.paganini2008.springplayer.channel.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.paganini2008.devtools.enums.EnumConstant;
import com.github.paganini2008.devtools.enums.EnumUtils;

/**
 * 
 * TemplateFormat
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public enum TemplateFormat implements EnumConstant {

	TEXT(0, "纯文本"), HTML(1, "HTML"), MARKDOWN(2, "Markdown"), FREEMARK(3, "Freemark"), THYMELEAF(4, "Thymeleaf");

	private TemplateFormat(int value, String repr) {
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
	public static TemplateFormat valueOf(Integer type) {
		return EnumUtils.valueOf(TemplateFormat.class, type);
	}

}
