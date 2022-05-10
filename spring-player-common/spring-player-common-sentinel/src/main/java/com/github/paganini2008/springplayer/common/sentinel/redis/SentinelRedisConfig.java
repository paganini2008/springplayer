package com.github.paganini2008.springplayer.common.sentinel.redis;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.cloud.sentinel.custom.SentinelAutoConfiguration;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.github.paganini2008.springplayer.common.sentinel.RuleManager;
import com.github.paganini2008.springplayer.common.sentinel.RuleType;
import com.github.paganini2008.springplayer.common.sentinel.SentinelRuleAutoInitializer;
import com.github.paganini2008.springplayer.common.sentinel.SentinelRuleProperties;

/**
 * 
 * SentinelRedisConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(SentinelRuleProperties.class)
@ConditionalOnProperty(name = "yl.platform.sentinel.rule.store", havingValue = "redis")
@ConditionalOnClass({ RedisOperations.class, SentinelAutoConfiguration.class, SentinelConfig.class })
@ConditionalOnBean(RedisConnectionFactory.class)
public class SentinelRedisConfig {

	@Bean
	public RuleManager redisRuleManager(RedisTemplate<String, Object> redisTemplate,
			SentinelRuleUpdateEventListenerContainer listenerContainer) {
		return new RedisRuleManager(redisTemplate, listenerContainer);
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.authority-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer authorityRuleInitializer(SentinelRuleProperties sentinelRedisProperties,
			RedisRuleManager redisRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRedisProperties.getAuthorityRuleKeys(), RuleType.AUTHORITY, redisRuleManager);
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.degrade-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer degradeRuleInitializer(SentinelRuleProperties sentinelRedisProperties,
			RedisRuleManager redisRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRedisProperties.getDegradeRuleKeys(), RuleType.DEGRADE, redisRuleManager);
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.system-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer systemRuleInitializer(SentinelRuleProperties sentinelRedisProperties,
			RedisRuleManager redisRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRedisProperties.getSystemRuleKeys(), RuleType.SYSTEM, redisRuleManager);
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.flow-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer flowRuleInitializer(SentinelRuleProperties sentinelRedisProperties,
			RedisRuleManager redisRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRedisProperties.getFlowRuleKeys(), RuleType.FLOW, redisRuleManager);
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.param-flow-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer paramFlowRuleInitializer(SentinelRuleProperties sentinelRedisProperties,
			RedisRuleManager redisRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRedisProperties.getParamFlowRuleKeys(), RuleType.PARAM_FLOW, redisRuleManager);
	}

	@ComponentScan("com.yl.platform.common.sentinel.redis")
	@Configuration
	public static class EmbeddedConfig {
	}

}
