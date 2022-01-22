
package com.github.paganini2008.springplayer.oauth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paganini2008.springplayer.security.ErrorMessageSource;
import com.github.paganini2008.springplayer.security.FailureAuthenticationEntryPoint;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

/**
 * 
 * AuthServerConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	private final AuthServerJdbcClientDetailsService clientDetailsService;

	private final AuthenticationManager authenticationManagerBean;

	private final UpmsUserDetailsService userDetailsService;

	private final AuthorizationCodeServices authorizationCodeServices;

	private final ObjectMapper objectMapper;

	private final ErrorMessageSource errorMessageSource;

	@Override
	@SneakyThrows
	public void configure(ClientDetailsServiceConfigurer clients) {
		clients.withClientDetails(clientDetailsService);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
		oauthServer.allowFormAuthenticationForClients()
				.authenticationEntryPoint(new FailureAuthenticationEntryPoint(objectMapper, errorMessageSource))
				.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
				.authenticationManager(authenticationManagerBean).userDetailsService(userDetailsService)
				.authorizationCodeServices(authorizationCodeServices).tokenStore(tokenStore()).accessTokenConverter(jwtAccessTokenConverter())
				.tokenEnhancer(tokenEnhancer()).exceptionTranslator(new OpenApiWebResponseExceptionTranslator());
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("123456");
		return accessTokenConverter;
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new AuthServerTokenEnhancer();
	}
}
