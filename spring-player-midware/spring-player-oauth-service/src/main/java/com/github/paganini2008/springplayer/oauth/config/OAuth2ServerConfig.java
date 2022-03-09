
package com.github.paganini2008.springplayer.oauth.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paganini2008.springplayer.common.oauth.FailureAuthenticationEntryPoint;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

/**
 * 
 * OAuth2ServerConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {

	private final OAuth2ServerClientDetailsService clientDetailsService;

	private final UpmsUserDetailsService userDetailsService;

	private final AuthenticationManager authenticationManagerBean;

	private final AuthorizationCodeServices authorizationCodeServices;

	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	public void configure(ClientDetailsServiceConfigurer clients) {
		clients.withClientDetails(clientDetailsService);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
		oauthServer.passwordEncoder(passwordEncoder()).allowFormAuthenticationForClients()
				.authenticationEntryPoint(new FailureAuthenticationEntryPoint(objectMapper)).checkTokenAccess("isAuthenticated()")
				.tokenKeyAccess("permitAll()");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));
		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
				.authenticationManager(authenticationManagerBean).userDetailsService(userDetailsService)
				.authorizationCodeServices(authorizationCodeServices).tokenStore(tokenStore())
				.accessTokenConverter(jwtAccessTokenConverter()).tokenEnhancer(enhancerChain)
				.exceptionTranslator(new OAuth2ServerExceptionTranslator());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("oauth2-jwt.jks"), "123456".toCharArray());
		accessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("oauth2-jwt"));
		return accessTokenConverter;
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new OAuth2ServerTokenEnhancer();
	}
}
