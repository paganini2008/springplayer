package com.github.paganini2008.springplayer.channel.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.github.paganini2008.springplayer.channel.enums.TemplateFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * TemplateDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class TemplateDTO {

	private Long id;

	@NotBlank(message = "模板名称不能为空")
	private String name;

	@NotBlank(message = "模板内容不能为空")
	private String content;

	@NotNull(message = "模板格式不能为空")
	private TemplateFormat format;

}
