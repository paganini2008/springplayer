package com.github.paganini2008.springplayer.security.info;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * PermissionInfo
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class PermissionInfo {

	private Long id;
	private String name;
	private String code;
	private String path;
	
}
