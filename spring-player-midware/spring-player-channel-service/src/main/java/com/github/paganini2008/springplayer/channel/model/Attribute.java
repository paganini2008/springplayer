package com.github.paganini2008.springplayer.channel.model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.paganini2008.springplayer.channel.enums.AttributeType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Attribute
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@TableName("infra_attribute")
@Getter
@Setter
public class Attribute {

	@TableId
	private Long id;

	@TableField("name")
	private String name;

	@TableField("var_type")
	private AttributeType varType;

	@TableField("default_value")
	private String defaultValue;

	@TableField("create_time")
	private LocalDateTime createTime;

	@TableField("update_time")
	private LocalDateTime updateTime;

}
