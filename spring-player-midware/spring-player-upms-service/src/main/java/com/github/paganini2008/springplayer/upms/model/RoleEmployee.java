package com.github.paganini2008.springplayer.upms.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * RoleEmployee
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@TableName("sys_role_emp")
public class RoleEmployee {

	@TableId
	private Long id;

	@TableField("role_id")
	private Long roleId;

	@TableField("emp_id")
	private Long employeeId;

}
