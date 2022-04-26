package com.alibaba.csp.sentinel.dashboard.repository.rule.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.repository.rule.RuleRepository;

/**
 * 
 * RedisRuleRepositoryConfig
 *
 * @author Feng Yan
 *
 * @version 2.0.5
 */
@ConditionalOnProperty(name = "sentinel.dashboard.rule.repository", havingValue = "redis")
@Configuration
public class RedisRuleRepositoryConfig {

	@Bean
	public RuleRepository<AuthorityRuleEntity, Long> authorityRuleStore(
			@Qualifier("sentinelRuleRedisTemplate") RedisTemplate<String, Object> redisTemplate, RedisAtomicLong idGen) {
		return new RedisAuthorityRuleStore(redisTemplate, idGen);
	}

	@Bean
	public RuleRepository<DegradeRuleEntity, Long> degradeRuleStore(
			@Qualifier("sentinelRuleRedisTemplate") RedisTemplate<String, Object> redisTemplate, RedisAtomicLong idGen) {
		return new RedisDegradeRuleStore(redisTemplate, idGen);
	}

	@Bean
	public RuleRepository<FlowRuleEntity, Long> flowRuleStore(
			@Qualifier("sentinelRuleRedisTemplate") RedisTemplate<String, Object> redisTemplate, RedisAtomicLong idGen) {
		return new RedisFlowRuleStore(redisTemplate, idGen);
	}

	@Bean
	public RuleRepository<ParamFlowRuleEntity, Long> paramFlowRuleStore(
			@Qualifier("sentinelRuleRedisTemplate") RedisTemplate<String, Object> redisTemplate, RedisAtomicLong idGen) {
		return new RedisParamFlowRuleStore(redisTemplate, idGen);
	}

	@Bean
	public RuleRepository<SystemRuleEntity, Long> systemRuleStore(
			@Qualifier("sentinelRuleRedisTemplate") RedisTemplate<String, Object> redisTemplate, RedisAtomicLong idGen) {
		return new RedisSystemRuleStore(redisTemplate, idGen);
	}

}
