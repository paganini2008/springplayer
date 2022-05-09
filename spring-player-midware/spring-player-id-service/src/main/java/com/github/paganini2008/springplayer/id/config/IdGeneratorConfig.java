package com.github.paganini2008.springplayer.id.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.github.paganini2008.springplayer.common.id.IdGeneratorFactory;
import com.github.paganini2008.springplayer.common.id.api.SnowFlakeIdGeneratorFactory;

/**
 * 
 * IdGeneratorConfig
 *
 * @author Feng Yan
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

}
