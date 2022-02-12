package com.github.paganini2008.springplayer.channel.model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.paganini2008.springplayer.channel.enums.TemplateFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Template
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@TableName("infra_template")
@Getter
@Setter
public class Template {

	@TableId
	private Long id;

	@TableField("name")
	private String name;

	@TableField("content")
	private String content;

	@TableField("format")
	private TemplateFormat format;

	@TableField("create_time")
	private LocalDateTime createTime;

	@TableField("update_time")
	private LocalDateTime updateTime;

}
