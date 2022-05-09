package com.github.paganini2008.springplayer.common.email;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * TextEmail
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class TextEmail implements Serializable {

	private static final long serialVersionUID = -2272899747900677355L;

	private String subject;
	private String from;
	private String[] to;
	private String[] bcc;
	private String[] cc;
	private String replyTo;

	private String template;
	private Map<String, Object> variables;

}
