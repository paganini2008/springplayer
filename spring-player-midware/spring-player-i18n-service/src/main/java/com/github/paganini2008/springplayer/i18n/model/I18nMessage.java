package com.github.paganini2008.springplayer.i18n.model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * I18nMessage
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@TableName("sys_i18n")
public class I18nMessage {

	@TableId
	private Long id;

	@TableField("project")
	private String project;

	@TableField("lang")
	private String lang;

	@TableField("code")
	private String code;

	@TableField("text")
	private String text;

	@TableField("create_time")
	private LocalDateTime createTime;

	@TableField("update_time")
	private LocalDateTime updateTime;

}
