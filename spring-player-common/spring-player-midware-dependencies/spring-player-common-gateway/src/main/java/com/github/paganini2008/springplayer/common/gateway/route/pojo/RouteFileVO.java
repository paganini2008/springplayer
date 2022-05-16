package com.github.paganini2008.springplayer.common.gateway.route.pojo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * RouteFileVO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class RouteFileVO {

	private Long id;

	/** 文件名 **/
	private String fileName;

	/** 文件内容 **/
	private String content;

	/** 路由规则文本格式 **/
	private String format;

	/** 部署环境标识 **/
	private String env;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

}
