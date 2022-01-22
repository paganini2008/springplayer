package com.github.paganini2008.springplayer.oauth.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.github.paganini2008.springplayer.security.info.CurrentUser;

/**
 * 
 * AuthServerTokenEnhancer
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class AuthServerTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		if (authentication.getUserAuthentication() != null) {
			Map<String, Object> info = new HashMap<>();
			CurrentUser user = (CurrentUser) authentication.getUserAuthentication().getPrincipal();
			info.put("user", user);
			info.put("license", "Apache Spring Player");
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		}
		return accessToken;
	}

}