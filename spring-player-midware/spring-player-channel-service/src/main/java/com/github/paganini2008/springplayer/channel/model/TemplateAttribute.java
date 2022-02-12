package com.github.paganini2008.springplayer.channel.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * TemplateAttribute
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@TableName("infra_template_attribute")
@Getter
@Setter
public class TemplateAttribute {

	@TableId
	private Long id;

	@TableField("template_id")
	private Long templateId;

	@TableField("attribute_id")
	private Long attributeId;

}
