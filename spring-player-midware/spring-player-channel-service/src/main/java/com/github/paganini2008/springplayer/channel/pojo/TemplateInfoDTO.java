package com.github.paganini2008.springplayer.channel.pojo;

import javax.validation.constraints.NotNull;

import com.github.paganini2008.springplayer.channel.enums.TemplateFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * TemplateInfoDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class TemplateInfoDTO {

	private Long id;
	private String name;

	@NotNull(message = "模板格式不能为空")
	private TemplateFormat format;
	
}
