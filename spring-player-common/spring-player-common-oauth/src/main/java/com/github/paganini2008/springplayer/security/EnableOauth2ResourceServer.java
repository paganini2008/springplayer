package com.github.paganini2008.springplayer.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.github.paganini2008.springplayer.feign.EnableEnhancedFeignClients;

/**
 * 
 * EnableOauth2ResourceServer
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Documented
@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@EnableResourceServer
@EnableEnhancedFeignClients
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({ Oauth2ResourceServerConfig.class })
public @interface EnableOauth2ResourceServer {

}
