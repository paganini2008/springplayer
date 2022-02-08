package com.github.paganini2008.springplayer.gateway.trace;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

/**
 * 
 * GatewayTraceConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class GatewayTraceConfig {

	@Bean
	public RedisAtomicLong spanGen(RedisConnectionFactory connectionFactory) {
		return new RedisAtomicLong("springplayer:web:crumb:spanGen", connectionFactory);
	}

}
