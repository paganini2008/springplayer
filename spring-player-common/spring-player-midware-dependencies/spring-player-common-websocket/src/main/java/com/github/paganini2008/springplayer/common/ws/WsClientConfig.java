package com.github.paganini2008.springplayer.common.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * WsClientConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class WsClientConfig {

	@Bean
	public WsConnectionPool wsConnectionPool() {
		return new WsConnectionPool();
	}
}
