package com.github.paganini2008.springplayer.oauth.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.beans.BeanUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.security.ErrorCodes;
import com.github.paganini2008.springplayer.security.info.CurrentUser;
import com.github.paganini2008.springplayer.security.info.DeptInfo;
import com.github.paganini2008.springplayer.security.info.OrgInfo;
import com.github.paganini2008.springplayer.security.info.PermissionInfo;
import com.github.paganini2008.springplayer.security.info.RoleInfo;
import com.github.paganini2008.springplayer.security.info.UserInfo;
import com.github.paganini2008.springplayer.upms.api.RemoteUserService;
import com.github.paganini2008.springplayer.upms.vo.UserVO;

import lombok.RequiredArgsConstructor;

/**
 * 
 * UpmsUserDetailsService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UpmsUserDetailsService implements UserDetailsService {

	private final RemoteUserService remoteUserService;
	private final UserDetailsChecker userDetailsChecker;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApiResult<UserVO> result = remoteUserService.getUserInfo(username);
		if (result == null || result.getData() == null) {
			throw new AuthServerException(ErrorCodes.USER_NOT_FOUND);
		}
		UserInfo userInfo = convertToUserInfo(result.getData());
		Set<String> authSet = new HashSet<>();
		if (ArrayUtils.isNotEmpty(userInfo.getRoles())) {
			Arrays.stream(userInfo.getRoles()).forEach(roleInfo -> authSet.add("ROLE_" + roleInfo.getCode().toUpperCase()));
		}
		if (ArrayUtils.isNotEmpty(userInfo.getPermissions())) {
			Arrays.stream(userInfo.getPermissions()).forEach(permInfo -> authSet.add("PERM_" + permInfo.getCode().toUpperCase()));
		}
		Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(authSet.toArray(new String[0]));
		CurrentUser user = new CurrentUser(userInfo, authorities);
		userDetailsChecker.check(user);
		return user;
	}

	private UserInfo convertToUserInfo(UserVO user) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(user.getId());
		userInfo.setFullname(user.getFullname());
		userInfo.setUsername(user.getUsername());
		userInfo.setPassword(user.getPassword());
		userInfo.setEnabled(user.getEnabled());
		userInfo.setAccountNonExpired(user.getAccountNonExpired());
		userInfo.setCredentialsNonExpired(user.getCredentialsNonExpired());
		userInfo.setAccountNonLocked(user.getAccountNonLocked());

		if (user.getEnterprise() != null) {
			OrgInfo org = new OrgInfo();
			BeanUtils.copyProperties(user.getEnterprise(), org);
			userInfo.setOrg(org);
		}

		if (user.getDept() != null) {
			DeptInfo dept = new DeptInfo();
			BeanUtils.copyProperties(user.getDept(), dept);
			userInfo.setDept(dept);
		}

		if (ArrayUtils.isNotEmpty(user.getRoles())) {
			userInfo.setRoles(Arrays.stream(user.getRoles()).map(roleVO -> {
				RoleInfo role = new RoleInfo();
				BeanUtils.copyProperties(roleVO, role);
				return role;
			}).toArray(l -> new RoleInfo[l]));
		}

		if (ArrayUtils.isNotEmpty(user.getPermissions())) {
			userInfo.setPermissions(Arrays.stream(user.getPermissions()).map(permissionVO -> {
				PermissionInfo permission = new PermissionInfo();
				BeanUtils.copyProperties(permissionVO, permission);
				return permission;
			}).toArray(l -> new PermissionInfo[l]));
		}

		return userInfo;
	}

}
