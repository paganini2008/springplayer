package com.github.paganini2008.springplayer.oauth;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * CurrentUser
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class SpUser extends User {

	private static final long serialVersionUID = 9139995303145562034L;

	public SpUser(UserInfo user, Collection<? extends GrantedAuthority> authorities) {
		super(user.getUsername(), user.getPassword(), user.getLocked(), true, true, true, authorities);
		this.id = user.getId();
		this.org = user.getOrg();
		this.dept = user.getDept();
		this.roles = user.getRoles();
		this.permissions = user.getPermissions();
	}

	private Long id;
	private OrgInfo org;
	private DeptInfo dept;
	private RoleInfo[] roles;
	private PermissionInfo[] permissions;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;

}
