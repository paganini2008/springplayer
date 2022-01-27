package com.github.paganini2008.springplayer.email;

import java.io.File;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * RichTextEmail
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class RichTextEmail extends TextEmail {

	private static final long serialVersionUID = -939797093383746765L;
	
	private RichTextType richTextType = RichTextType.THYMELEAF;
	private Map<String, File> attachments;

}
