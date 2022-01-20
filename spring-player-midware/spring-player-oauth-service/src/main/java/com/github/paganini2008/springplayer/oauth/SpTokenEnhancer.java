package com.github.paganini2008.springplayer.oauth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * 
 * SpringPlayerTokenEnhancer
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class SpTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Map<String, Object> info = new HashMap<>();
		SpUser spUser = (SpUser) authentication.getUserAuthentication().getPrincipal();
		info.put("user", spUser);
		info.put("license", "Spring Player");
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		return accessToken;
	}

}
