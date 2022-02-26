package com.github.paganini2008.springplayer.crumb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.github.paganini2008.springplayer.id.IdGenerator;
import com.github.paganini2008.springplayer.id.RedisGlobalIdGenerator;

/**
 * 
 * CrumbConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class CrumbConfig {

	@Bean
	public IdGenerator idGenerator(RedisConnectionFactory redisConnectionFactory) {
		return new RedisGlobalIdGenerator(redisConnectionFactory);
	}

}
