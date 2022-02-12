package com.github.paganini2008.springplayer.messenger.entity;

import java.util.Map;

import com.github.paganini2008.springplayer.email.RichTextType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * EmailEntity
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class EmailEntity {

	private String subject;
	private String from;
	private String[] to;
	private String[] bcc;
	private String[] cc;
	private String replyTo;

	private String template;
	private Map<String, Object> variables;
	private Integer type;

	public RichTextType getType() {
		return type != null ? RichTextType.valueOf(type) : null;
	}

}
