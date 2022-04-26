package com.github.paganini2008.springplayer.common.gateway.route.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * FlushOperationDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class FlushOperationDTO extends BatchOperationDTO {

	private boolean cascade = true;

}
