package com.alibaba.csp.sentinel.dashboard.repository.rule.jdbc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.mapper.SentinelRuleMapper;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.repository.rule.RuleRepository;

/**
 * 
 * JdbcRuleRepositoryConfig
 *
 * @author Feng Yan
 *
 * @version 2.0.5
 */
@ConditionalOnProperty(name = "sentinel.dashboard.rule.repository", havingValue = "jdbc", matchIfMissing = true)
@MapperScan(basePackages = { "com.alibaba.csp.sentinel.dashboard.datasource.entity.mapper" })
@Configuration
public class JdbcRuleRepositoryConfig {

	@Bean
	public RuleRepository<AuthorityRuleEntity, Long> authorityRuleStore(SentinelRuleService sentinelRuleService, RedisAtomicLong idGen) {
		return new JdbcAuthorityRuleStore(sentinelRuleService, idGen);
	}

	@Bean
	public RuleRepository<DegradeRuleEntity, Long> degradeRuleStore(SentinelRuleService sentinelRuleService, RedisAtomicLong idGen) {
		return new JdbcDegradeRuleStore(sentinelRuleService, idGen);
	}

	@Bean
	public RuleRepository<FlowRuleEntity, Long> flowRuleStore(SentinelRuleService sentinelRuleService, RedisAtomicLong idGen) {
		return new JdbcFlowRuleStore(sentinelRuleService, idGen);
	}

	@Bean
	public RuleRepository<ParamFlowRuleEntity, Long> paramFlowRuleStore(SentinelRuleService sentinelRuleService, RedisAtomicLong idGen) {
		return new JdbcParamFlowRuleStore(sentinelRuleService, idGen);
	}

	@Bean
	public RuleRepository<SystemRuleEntity, Long> systemRuleStore(SentinelRuleService sentinelRuleService, RedisAtomicLong idGen) {
		return new JdbcSystemRuleStore(sentinelRuleService, idGen);
	}

	@Bean
	public SentinelRuleService sentinelRuleService(SentinelRuleMapper sentinelRuleDao) {
		return new SentinelRuleServiceImpl(sentinelRuleDao);
	}

}
