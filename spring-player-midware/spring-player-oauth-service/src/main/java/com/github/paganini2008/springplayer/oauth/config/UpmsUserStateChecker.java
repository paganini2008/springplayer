
package com.github.paganini2008.springplayer.oauth.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import com.github.paganini2008.springplayer.common.oauth.ErrorCodes;

/**
 * 
 * UpmsUserStateChecker
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class UpmsUserStateChecker implements UserDetailsChecker {

	@Override
	public void check(UserDetails user) {
		if (!user.isEnabled()) {
			throw new UpmsUserStateException(ErrorCodes.ACCOUNT_DISABLED);
		}
		if (!user.isAccountNonLocked()) {
			throw new UpmsUserStateException(ErrorCodes.ACCOUNT_LOCKED);
		}
		if (!user.isAccountNonExpired()) {
			throw new UpmsUserStateException(ErrorCodes.ACCOUNT_EXPIRED);
		}
		if (!user.isCredentialsNonExpired()) {
			throw new UpmsUserStateException(ErrorCodes.CREDENTIALS_EXPIRED);
		}
	}

}
