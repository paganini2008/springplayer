
package com.github.paganini2008.springplayer.common.oauth.utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.paganini2008.springplayer.common.oauth.info.UpmsUser;

import lombok.experimental.UtilityClass;

/**
 * 
 * SecurityUtils
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@UtilityClass
public class SecurityUtils {

	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public UpmsUser getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof UpmsUser) {
			return (UpmsUser) principal;
		}
		return null;
	}

	public UpmsUser currentUser() {
		Authentication authentication = getAuthentication();
		return getUser(authentication);
	}

	public List<String> getRoles() {
		Authentication authentication = getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.stream().filter(granted -> granted.getAuthority().startsWith("ROLE_"))
				.map(granted -> granted.getAuthority().replaceFirst("ROLE_", "")).collect(Collectors.toList());
	}

}
