package com.alibaba.csp.sentinel.dashboard.datasource.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 
 * SentinelRule
 *
 * @author Fred Feng
 *
 * @version 2.0.5
 */
@TableName("sentinel_rule")
public class SentinelRule {

	@TableId
	@TableField("id")
	private Long id;

	@TableField("rule_type")
	private Integer type;

	@TableField("app_name")
	private String appName;

	@TableField("ip_addr")
	private String ipAddr;

	@TableField("port")
	private Integer port;

	@TableField("content")
	private String content;

	@TableField("created_time")
	private LocalDateTime createdTime;

	@TableField("updated_time")
	private LocalDateTime updatedTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

}
