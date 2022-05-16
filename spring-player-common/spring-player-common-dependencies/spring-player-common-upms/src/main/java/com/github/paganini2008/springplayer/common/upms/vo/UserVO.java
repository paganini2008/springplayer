package com.github.paganini2008.springplayer.common.upms.vo;

import lombok.Data;

/**
 * 
 * UserVO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Data
public class UserVO {

	private Long id;
	private String fullname;
	private String username;
	private String password;
	private Boolean enabled;
	private Boolean accountNonExpired = true;
	private Boolean credentialsNonExpired = true;
	private Boolean accountNonLocked = true;

	private EnterpriseVO enterprise;
	private DeptVO dept;
	private RoleVO[] roles;
	private PermissionVO[] permissions;

	public UserVO() {
	}

	public UserVO(Long id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

}
