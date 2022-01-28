
package com.github.paganini2008.springplayer.security;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkException;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

/**
 * 
 * AuthResourceServerConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@AllArgsConstructor
@Configuration
public class AuthResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String PUBLIC_CERT = "public.cert";

	private final TokenExtractor tokenExtractor;
	private final WhiteListProperties whiteListProperties;
	private final ObjectMapper objectMapper;

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
		resources.tokenStore(tokenStore()).authenticationEntryPoint(new FailureAuthenticationEntryPoint(objectMapper))
				.tokenExtractor(tokenExtractor).accessDeniedHandler(new GlobalAccessDeniedHandler(objectMapper));
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		Resource resource = new ClassPathResource(PUBLIC_CERT);
		String publicKey;
		try {
			publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
		} catch (IOException e) {
			throw new JwkException(e.getMessage(), e);
		}
		converter.setVerifierKey(publicKey);
		return converter;
	}
}
