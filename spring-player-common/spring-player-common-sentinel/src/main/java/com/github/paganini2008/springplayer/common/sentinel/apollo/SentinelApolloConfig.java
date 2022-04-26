package com.github.paganini2008.springplayer.common.sentinel.apollo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

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
 * SentinelApolloConfig
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@EnableConfigurationProperties(SentinelRuleProperties.class)
@ConditionalOnClass({ Config.class, SentinelAutoConfiguration.class, SentinelConfig.class })
@ConditionalOnProperty(name = "yl.platform.sentinel.rule.store", havingValue = "apollo", matchIfMissing = true)
public class SentinelApolloConfig {

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
