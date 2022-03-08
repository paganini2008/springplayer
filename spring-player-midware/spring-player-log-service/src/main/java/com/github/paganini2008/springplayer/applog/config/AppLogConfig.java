package com.github.paganini2008.springplayer.applog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.github.paganini2008.springplayer.id.IdGenerator;
import com.github.paganini2008.springplayer.id.RedisIdGenerator;

/**
 * 
 * AppLogConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class AppLogConfig {

	@Bean
	public IdGenerator idGenerator(RedisConnectionFactory redisConnectionFactory) {
		return new RedisIdGenerator(redisConnectionFactory);
	}

}
