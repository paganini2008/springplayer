package com.github.paganini2008.springplayer.oauth;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.beans.BeanUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.upms.api.RemoteUserService;
import com.github.paganini2008.springplayer.upms.vo.UserVO;

import lombok.RequiredArgsConstructor;

/**
 * 
 * SpUserDetailsService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class SpUserDetailsService implements UserDetailsService {

	private final RemoteUserService remoteUserService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApiResult<UserVO> result = remoteUserService.getUserInfo(username);
		if (result == null || result.getData() == null) {
			throw new UsernameNotFoundException("用户不存在!");
		}
		UserInfo userInfo = convertToUserInfo(result.getData());
		Set<String> authSet = new HashSet<>();
		if (ArrayUtils.isNotEmpty(userInfo.getRoles())) {
			Arrays.stream(userInfo.getRoles()).forEach(roleInfo -> authSet.add(roleInfo.getCode()));
		}
		if (ArrayUtils.isNotEmpty(userInfo.getPermissions())) {
			Arrays.stream(userInfo.getPermissions()).forEach(permInfo -> authSet.add(permInfo.getCode()));
		}
		Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(authSet.toArray(new String[0]));
		userInfo = getUserInfo();
		return new SpUser(userInfo, authorities);
	}
	
	protected UserInfo getUserInfo() {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(1L);
		userInfo.setUsername("hadoop");
		userInfo.setPassword("{bcrypt}$2a$10$d9DWtD8sS6PvkIGhyBLyKOkVtZ0.unuQD1Rx91zxERcCCskjFod/.");
		//userInfo.setPassword("123456");
		userInfo.setLocked(true);
		return userInfo;
	}

	private UserInfo convertToUserInfo(UserVO user) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(user.getId());
		userInfo.setFullname(user.getFullname());
		userInfo.setUsername(user.getUsername());
		userInfo.setPassword(user.getPassword());
		userInfo.setLocked(user.getEnabled());

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
