package com.github.paganini2008.springplayer.common.sentinel.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
 * SentinelRuleRedisConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ConditionalOnProperty(name = "spring.cloud.sentinel.rule.store", havingValue = "redis")
@ConditionalOnClass({ RedisOperations.class, SentinelAutoConfiguration.class, SentinelConfig.class })
@ConditionalOnBean(RedisConnectionFactory.class)
@EnableConfigurationProperties({ SentinelRuleProperties.class })
@Configuration(proxyBeanMethods = false)
public class SentinelRuleRedisConfig {

	@Bean
	public RuleManager redisRuleManager(RedisTemplate<String, Object> redisTemplate, SentinelRuleUpdateEventListenerContainer container) {
		return new RedisRuleManager(redisTemplate, container);
	}

	@Bean
	public SentinelRuleUpdateEventListenerContainer sentinelRuleUpdateEventListenerContainer() {
		return new SentinelRuleUpdateEventListenerContainer();
	}
	
	@Bean
	public SentinelRuleEventPublisher sentinelRuleEventPublisher() {
		return new SentinelRuleEventPublisher();
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.authority-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer authorityRuleInitializer(SentinelRuleProperties sentinelRuleProperties,
			RedisRuleManager redisRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRuleProperties.getAuthorityRuleKeys(), RuleType.AUTHORITY, redisRuleManager);
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.degrade-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer degradeRuleInitializer(SentinelRuleProperties sentinelRuleProperties,
			RedisRuleManager redisRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRuleProperties.getDegradeRuleKeys(), RuleType.DEGRADE, redisRuleManager);
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.system-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer systemRuleInitializer(SentinelRuleProperties sentinelRuleProperties,
			RedisRuleManager redisRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRuleProperties.getSystemRuleKeys(), RuleType.SYSTEM, redisRuleManager);
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.flow-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer flowRuleInitializer(SentinelRuleProperties sentinelRuleProperties,
			RedisRuleManager redisRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRuleProperties.getFlowRuleKeys(), RuleType.FLOW, redisRuleManager);
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.param-flow-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer paramFlowRuleInitializer(SentinelRuleProperties sentinelRuleProperties,
			RedisRuleManager redisRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRuleProperties.getParamFlowRuleKeys(), RuleType.PARAM_FLOW, redisRuleManager);
	}

}
