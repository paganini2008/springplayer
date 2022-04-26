package com.github.paganini2008.springplayer.common.gateway.route.pojo;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * BatchOperationDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class BatchOperationDTO {

	@NotEmpty(message = "请先选择要操作的组名")
	private String[] groupNames;

}
