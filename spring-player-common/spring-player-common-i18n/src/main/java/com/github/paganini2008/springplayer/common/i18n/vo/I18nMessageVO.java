package com.github.paganini2008.springplayer.common.i18n.vo;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 
 * I18nMessageVO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Data
public class I18nMessageVO {

	private Long id;

	private String project;

	private String lang;

	private String code;

	private String text;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;
	
}
