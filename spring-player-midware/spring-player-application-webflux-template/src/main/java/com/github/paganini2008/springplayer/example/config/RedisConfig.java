package com.github.paganini2008.springplayer.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.github.paganini2008.springplayer.common.redis.lock.RedisSharedLock;

/**
 * 
 * RedisConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration
public class RedisConfig {

	@Bean
	public RedisSharedLock sharedLock(RedisConnectionFactory redisConnectionFactory) {
		return new RedisSharedLock("test-lock", redisConnectionFactory);
	}
	
}
