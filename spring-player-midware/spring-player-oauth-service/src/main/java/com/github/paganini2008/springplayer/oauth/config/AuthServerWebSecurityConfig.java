package com.github.paganini2008.springplayer.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paganini2008.springplayer.security.FailureAuthenticationEntryPoint;
import com.github.paganini2008.springplayer.security.GlobalAccessDeniedHandler;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

/**
 * 
 * AuthServerWebSecurityConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Primary
@Order(90)
@Configuration
@AllArgsConstructor
public class AuthServerWebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	protected void configure(HttpSecurity http) {
		http.csrf().disable().httpBasic().disable().cors().and().authorizeRequests().antMatchers("/oauth/**", "/actuator/**").permitAll()
				.anyRequest().authenticated().and().exceptionHandling()
				.authenticationEntryPoint(new FailureAuthenticationEntryPoint(objectMapper))
				.accessDeniedHandler(new GlobalAccessDeniedHandler(objectMapper));
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/static/**");
	}

	@Bean
	@Override
	@SneakyThrows
	public AuthenticationManager authenticationManagerBean() {
		return super.authenticationManagerBean();
	}

	@Bean
	public UserDetailsChecker userStateChecker() {
		return new UpmsUserStateChecker();
	}

	public static void main(String[] args) {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		String password = "123456";
		String encrpy = encoder.encode(password);
		System.out.println(encrpy);
		System.out.println(encoder.matches(password, encrpy));
	}

}
