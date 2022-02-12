package com.github.paganini2008.springplayer.logging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.github.paganini2008.springplayer.id.IdGenerator;
import com.github.paganini2008.springplayer.id.RedisGlobalIdGenerator;

/**
 * 
 * LoggingConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class LoggingConfig {

	@Bean
	public IdGenerator idGenerator(RedisConnectionFactory redisConnectionFactory) {
		return new RedisGlobalIdGenerator(redisConnectionFactory);
	}

}