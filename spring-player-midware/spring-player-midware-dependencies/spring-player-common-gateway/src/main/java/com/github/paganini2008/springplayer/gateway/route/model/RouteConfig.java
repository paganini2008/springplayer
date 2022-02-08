package com.github.paganini2008.springplayer.gateway.route.model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * RouteConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@TableName("sys_route")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RouteConfig {

	@TableId
	private Long id;

	/** 服务名 **/
	@EqualsAndHashCode.Include
	@TableField("service_id")
	private String serviceId;

	/** 组名：对应RouteFile的文件名 **/
	@EqualsAndHashCode.Include
	@TableField("group_name")
	private String groupName;

	/** 路由规则文本 **/
	@EqualsAndHashCode.Include
	@TableField("rule")
	private String rule;

	/** 部署环境标识 **/
	@EqualsAndHashCode.Include
	@TableField("env")
	private String env;

	@TableField("create_time")
	private LocalDateTime createTime;

	@TableField("update_time")
	private LocalDateTime updateTime;

}
