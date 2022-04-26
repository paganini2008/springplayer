package com.github.paganini2008.springplayer.common.gateway.route.model;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * RouteFile
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@TableName("sys_route_file")
@Getter
@Setter
@EqualsAndHashCode
public class RouteFile {

	@TableId
	private Long id;

	/** 文件名 **/
	@TableField("file_name")
	private String fileName;

	/** 文件内容 **/
	@TableField("content")
	private String content;

	/** 路由规则文本格式 **/
	@TableField("format")
	private String format;

	/** 部署环境标识 **/
	@TableField("env")
	private String env;

	@TableField("create_time")
	private LocalDateTime createTime;

	@TableField("update_time")
	private LocalDateTime updateTime;

	public String getIdentifier() {
		return id != null ? String.valueOf(id) : "";
	}

	public long getSize() {
		return StringUtils.isNoneBlank(content) ? content.getBytes().length : 0L;
	}

}
