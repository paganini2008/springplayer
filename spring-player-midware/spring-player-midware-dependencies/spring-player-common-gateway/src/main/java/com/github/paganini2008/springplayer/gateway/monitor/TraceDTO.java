package com.github.paganini2008.springplayer.gateway.monitor;

import javax.validation.constraints.NotBlank;

import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * TraceDTO
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@Getter
@Setter
public class TraceDTO {

	@NotBlank(message = "路径不能为空")
	private String pathPattern;
	
	private @Nullable Label[] labels;
	
}
