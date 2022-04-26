package com.github.paganini2008.springplayer.common.gateway.route.pojo;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * RouteConfigGroupDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class RouteConfigGroupDTO {

	@NotBlank(message = "组名不能为空")
	private String groupName;
	
	private RouteConfigDTO[] configs;

}
