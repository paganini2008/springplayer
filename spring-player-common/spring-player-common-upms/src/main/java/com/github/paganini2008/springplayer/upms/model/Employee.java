package com.github.paganini2008.springplayer.upms.model;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Employee
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@TableName("sys_emp")
public class Employee {

	@TableId
	private Long id;
	
	@TableField("fullname")
	private String fullname;

	@TableField("username")
	private String username;

	@TableField("password")
	private String password;

	@TableField("position")
	private String position;

	@TableField("phone")
	private String phone;

	@TableField("email")
	private String email;

	@TableField("enterprise_id")
	private Long enterpriseId;

	@TableField("dept_id")
	private Long deptId;
	
	@TableField("enabled")
	private Integer enabled;

	@TableField("create_time")
	private LocalDateTime createTime;

	@TableField("update_time")
	private LocalDateTime updateTime;

}
