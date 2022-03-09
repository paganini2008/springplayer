package com.github.paganini2008.springplayer.common.id;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * 
 * IdGeneratorAutoConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class IdGeneratorAutoConfig {

	@Value("${spring.application.name}")
	private String applicationName;

	@ConditionalOnMissingBean
	@Bean
	public IdGeneratorFactory idGeneratorFactory(RedisConnectionFactory connectionFactory) {
		return new SnowFlakeIdGeneratorFactory(applicationName, connectionFactory);
	}

}
