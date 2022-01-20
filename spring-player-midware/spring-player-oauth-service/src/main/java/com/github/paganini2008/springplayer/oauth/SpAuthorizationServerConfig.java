package com.github.paganini2008.springplayer.oauth;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

/**
 * 
 * AuthorizationServerConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
//@EnableAuthorizationServer
//@Configuration
@AllArgsConstructor
public class SpAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	private final ClientDetailsService clientDetailsService;

	private final AuthenticationManager authenticationManager;

	private final UserDetailsService userDetailsService;

	private final AuthorizationCodeServices authorizationCodeServices;

	private final TokenStore tokenStore;

	private final TokenEnhancer tokenEnhancer;

	private final JwtAccessTokenConverter jwtAccessTokenConverter;

	private final ObjectMapper objectMapper;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.allowFormAuthenticationForClients()
				.authenticationEntryPoint(new SpAuthenticationEntryPoint(objectMapper))
				.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientDetailsService);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
				.authenticationManager(authenticationManager).userDetailsService(userDetailsService)
				.authorizationCodeServices(authorizationCodeServices).tokenStore(tokenStore)
				.tokenEnhancer(tokenEnhancer).accessTokenConverter(jwtAccessTokenConverter);
	}

}
