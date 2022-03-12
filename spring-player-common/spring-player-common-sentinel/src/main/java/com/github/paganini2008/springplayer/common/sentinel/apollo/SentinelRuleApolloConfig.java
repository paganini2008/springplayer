package com.github.paganini2008.springplayer.common.sentinel.apollo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.cloud.sentinel.custom.SentinelAutoConfiguration;
import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.ctrip.framework.apollo.Config;
import com.github.paganini2008.springplayer.common.sentinel.RuleManager;
import com.github.paganini2008.springplayer.common.sentinel.RuleType;
import com.github.paganini2008.springplayer.common.sentinel.SentinelRuleAutoInitializer;
import com.github.paganini2008.springplayer.common.sentinel.SentinelRuleProperties;

/**
 * 
 * SentinelRuleApolloConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration
@ConditionalOnClass({ Config.class, SentinelAutoConfiguration.class, SentinelConfig.class })
@ConditionalOnProperty(name = "spring.cloud.sentinel.rule.management.store", havingValue = "apollo", matchIfMissing = true)
public class SentinelRuleApolloConfig {

	@ConditionalOnMissingBean
	@Bean
	public SentinelResourceAspect sentinelResourceAspect() {
		return new SentinelResourceAspect();
	}

	@Bean
	public RuleManager apolloRuleManager() {
		return new ApolloRuleManager();
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.authority-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer authorityRuleInitializer(SentinelRuleProperties sentinelRuleProperties,
			ApolloRuleManager apolloRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRuleProperties.getAuthorityRuleKeys(), RuleType.AUTHORITY, apolloRuleManager);
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.degrade-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer degradeRuleInitializer(SentinelRuleProperties sentinelRuleProperties,
			ApolloRuleManager apolloRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRuleProperties.getDegradeRuleKeys(), RuleType.DEGRADE, apolloRuleManager);
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.system-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer systemRuleInitializer(SentinelRuleProperties sentinelRuleProperties,
			ApolloRuleManager apolloRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRuleProperties.getSystemRuleKeys(), RuleType.SYSTEM, apolloRuleManager);
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.flow-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer flowRuleInitializer(SentinelRuleProperties sentinelRuleProperties,
			ApolloRuleManager apolloRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRuleProperties.getFlowRuleKeys(), RuleType.FLOW, apolloRuleManager);
	}

	@ConditionalOnExpression("!'${spring.cloud.sentinel.rule.paramflow-rule-keys}'.isEmpty()")
	@Bean
	public SentinelRuleAutoInitializer paramFlowRuleInitializer(SentinelRuleProperties sentinelRuleProperties,
			ApolloRuleManager apolloRuleManager) {
		return new SentinelRuleAutoInitializer(sentinelRuleProperties.getParamFlowRuleKeys(), RuleType.PARAM_FLOW, apolloRuleManager);
	}

}
