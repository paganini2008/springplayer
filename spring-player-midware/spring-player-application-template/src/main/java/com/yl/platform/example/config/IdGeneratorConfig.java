package com.yl.platform.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.github.paganini2008.springplayer.id.IdGeneratorFactory;
import com.github.paganini2008.springplayer.id.SnowFlakeIdGeneratorFactory;
import com.github.paganini2008.springplayer.redis.RedisBloomFilter;
import com.github.paganini2008.springplayer.redis.lock.RedisSharedLock;

/**
 * 
 * IdGeneratorConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class IdGeneratorConfig {

	@Value("${spring.application.name}")
	private String applicationName;

	@Bean
	public IdGeneratorFactory idGeneratorFactory(RedisConnectionFactory connectionFactory) {
		return new SnowFlakeIdGeneratorFactory(applicationName, connectionFactory);
	}

	@Bean
	public RedisBloomFilter redisBloomFilter(RedisConnectionFactory connectionFactory) {
		return new RedisBloomFilter("test-bloom", 100000000, 0.03d, connectionFactory);
	}

	@Bean
	public RedisSharedLock RedisSharedLock(RedisConnectionFactory connectionFactory) {
		return new RedisSharedLock("test-lock", connectionFactory);
	}
}
