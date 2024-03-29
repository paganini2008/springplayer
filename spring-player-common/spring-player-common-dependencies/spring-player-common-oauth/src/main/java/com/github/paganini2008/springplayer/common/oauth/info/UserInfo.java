package com.github.paganini2008.springplayer.common.oauth.info;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * UserInfo
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class UserInfo {

	private Long id;
	private String fullname;
	private String username;
	private String password;
	private OrgInfo org;
	private DeptInfo dept;
	private RoleInfo[] roles;
	private PermissionInfo[] permissions;
	private Boolean enabled;
	private Boolean accountNonExpired;
	private Boolean credentialsNonExpired;
	private Boolean accountNonLocked;

}
