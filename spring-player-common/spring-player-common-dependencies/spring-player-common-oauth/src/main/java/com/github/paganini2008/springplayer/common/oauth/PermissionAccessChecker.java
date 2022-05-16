
package com.github.paganini2008.springplayer.common.oauth;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.StringUtils;

/**
 * 
 * PermissionAccessChecker
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component("perm")
public class PermissionAccessChecker {

	public boolean hasPermission(String... permissions) {
		if (ArrayUtils.isEmpty(permissions)) {
			return false;
		}
		if (SecurityContextHolder.getContext() == null) {
			return false;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtils::isNotBlank)
				.anyMatch(x -> PatternMatchUtils.simpleMatch(permissions, x));
	}

}
