package com.github.paganini2008.springplayer.common.gateway.sentinel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * ApiPathPredicateItemDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiPathPredicateItemDTO {

	private String pattern;
	
    private Integer matchStrategy;
	
}
