package com.github.paganini2008.springplayer.sentinel.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.github.paganini2008.springplayer.id.IdGenerator;
import com.github.paganini2008.springplayer.id.RedisGlobalIdGenerator;
import com.github.paganini2008.springplayer.sentinel.utils.JdbcInitializer;

/**
 * 
 * SentinelRuleConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class SentinelRuleConfig {
	
	@Bean
	public IdGenerator idGenerator(RedisConnectionFactory connectionFactory) {
		return new RedisGlobalIdGenerator(connectionFactory);
	}

	@Bean("sentinelRuleJdbcInitializer")
	public JdbcInitializer jdbcInitializer(DataSource dataSource) {
		final String[] tableNames = { "sys_sentinel_rule" };
		return new JdbcInitializer(dataSource, tableNames);
	}

}
