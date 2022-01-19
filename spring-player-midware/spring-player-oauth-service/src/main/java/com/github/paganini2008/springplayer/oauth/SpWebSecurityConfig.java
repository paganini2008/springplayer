package com.github.paganini2008.springplayer.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.SneakyThrows;

/**
 * 
 * SpWebSecurityConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Primary
@Order(90)
@Configuration(proxyBeanMethods = false)
public class SpWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	@SneakyThrows
	protected void configure(HttpSecurity http) {
		http.csrf().disable().httpBasic().disable().cors().and().authorizeRequests()
		.antMatchers("/token/**", "/actuator/**").permitAll().anyRequest().authenticated();
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/css/**");
	}

	@Bean
	@Override
	@SneakyThrows
	public AuthenticationManager authenticationManagerBean() {
		return super.authenticationManagerBean();
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	public static void main(String[] args) {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		String password = "123456";
		String encrpy = encoder.encode(password);
		System.out.println(encrpy);
		System.out.println(encoder.matches(password, encrpy));
	}

}
