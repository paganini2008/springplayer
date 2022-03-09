package com.github.paganini2008.springplayer.common.oauth.info;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * UpmsUser
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class UpmsUser extends User {

	private static final long serialVersionUID = 9139995303145562034L;

	public UpmsUser(UserInfo user, Collection<? extends GrantedAuthority> authorities) {
		super(user.getUsername(), user.getPassword(), user.getEnabled(), user.getAccountNonExpired(), user.getCredentialsNonExpired(),
				user.getAccountNonLocked(), authorities);
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

}
