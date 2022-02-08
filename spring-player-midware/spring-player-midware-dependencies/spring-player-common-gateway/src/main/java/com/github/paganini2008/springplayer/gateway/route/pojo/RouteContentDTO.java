package com.github.paganini2008.springplayer.gateway.route.pojo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 
 * RouteContentDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Data
public class RouteContentDTO {

	@NotBlank(message = "文件名不能为空")
	private String name;

	@NotBlank(message = "配置内容不能为空")
	private String rule = "# 请输入您的配置";

	@NotBlank(message = "文件格式不能为空")
	private String format;

}
