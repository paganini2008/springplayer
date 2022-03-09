package com.github.paganini2008.springplayer.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.github.paganini2008.springplayer.common.redis.lock.RedisSharedLock;

@Configuration
public class RedisConfig {

	@Bean
	public RedisSharedLock sharedLock(RedisConnectionFactory redisConnectionFactory) {
		return new RedisSharedLock("test-lock", redisConnectionFactory);
	}
	
}
