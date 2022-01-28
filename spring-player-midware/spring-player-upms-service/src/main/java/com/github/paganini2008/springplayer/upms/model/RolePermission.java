package com.github.paganini2008.springplayer.upms.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * RolePermission
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@TableName("sys_role_perm")
public class RolePermission {

	@TableId
	private Long id;

	@TableField("role_id")
	private Long roleId;
	
	@TableField("perm_id")
	private Long permissionId;
	
}
