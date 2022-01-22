
package com.github.paganini2008.springplayer.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.SneakyThrows;

/**
 * 
 * AuthResourceServerConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration
public class AuthResourceServerConfig extends ResourceServerConfigurerAdapter {

	private final AuthenticationEntryPoint authExceptionEntryPoint;
	private final TokenExtractor tokenExtractor;
	private final AccessDeniedHandler accessDeniedHandler;
	private final WhiteListProperties whiteListProperties;

	public AuthResourceServerConfig(AuthenticationEntryPoint authExceptionEntryPoint, TokenExtractor tokenExtractor,
			AccessDeniedHandler accessDeniedHandler, WhiteListProperties whiteListProperties) {
		this.authExceptionEntryPoint = authExceptionEntryPoint;
		this.tokenExtractor = tokenExtractor;
		this.accessDeniedHandler = accessDeniedHandler;
		this.whiteListProperties = whiteListProperties;
	}

	@Override
	@SneakyThrows
	public void configure(HttpSecurity httpSecurity) {
		httpSecurity.headers().frameOptions().disable();
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();
		whiteListProperties.getWhiteListUrls().forEach(url -> registry.antMatchers(url).permitAll());
		registry.anyRequest().authenticated().and().csrf().disable();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.tokenServices(tokenServices()).authenticationEntryPoint(authExceptionEntryPoint).tokenExtractor(tokenExtractor)
				.accessDeniedHandler(accessDeniedHandler);
	}

	@Value("${security.oauth2.server.jwt.signKey:123456}")
	private String signKey;

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(signKey);
		return converter;
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		return defaultTokenServices;
	}

}
