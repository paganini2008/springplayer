
package com.github.paganini2008.springplayer.security;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * 
 * DefaultPreAuthenticationChecks
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class DefaultPreAuthenticationChecker implements UserDetailsChecker {

	@Override
	public void check(UserDetails user) {
		if (!user.isEnabled()) {
			throw new DisabledException("用户未激活");
		}
		if (!user.isAccountNonLocked()) {
			throw new LockedException("用户帐号已被锁定");
		}
		if (!user.isAccountNonExpired()) {
			throw new AccountExpiredException("用户帐号已过期");
		}
		if (!user.isCredentialsNonExpired()) {
			throw new CredentialsExpiredException("用户凭证已过期");
		}
	}

}
