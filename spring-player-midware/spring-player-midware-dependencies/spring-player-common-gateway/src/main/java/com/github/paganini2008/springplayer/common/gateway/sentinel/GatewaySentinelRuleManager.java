package com.github.paganini2008.springplayer.common.gateway.sentinel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.springplayer.common.sentinel.redis.FixedRedisDataSource;
import com.github.paganini2008.springplayer.common.sentinel.redis.SentinelRulePublishEvent;
import com.github.paganini2008.springplayer.common.sentinel.redis.SentinelRuleUpdateEventListenerContainer;
import com.github.paganini2008.springplayer.common.utils.JacksonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * GatewaySentinelRuleManager
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewaySentinelRuleManager {

	public static final String SENTINEL_GATEWAY_RULE = "rule";
	public static final String SENTINEL_GATEWAY_RULE_API = "api";

	private final RedisTemplate<String, Object> redisTemplate;
	private final SentinelRuleUpdateEventListenerContainer sentinelRuleUpdateEventListenerContainer;

	public void loadGatewayFlowRules(String... ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}

		String name = Arrays.toString(ruleKeys) + ":" + SENTINEL_GATEWAY_RULE;
		sentinelRuleUpdateEventListenerContainer.unsubscribe(name);
		FixedRedisDataSource<Set<GatewayFlowRule>> rule = new FixedRedisDataSource<Set<GatewayFlowRule>>(redisTemplate, ruleKeys,
				sources -> ArrayUtils.isEmpty(sources) ? Collections.emptySet() : parseGatewayFlowRuleJsons(sources));
		GatewayRuleManager.register2Property(rule.getProperty());
		sentinelRuleUpdateEventListenerContainer.subscribe(name, rule);

		name = Arrays.toString(ruleKeys) + ":" + SENTINEL_GATEWAY_RULE_API;
		sentinelRuleUpdateEventListenerContainer.unsubscribe(name);
		FixedRedisDataSource<Set<ApiDefinition>> api = new FixedRedisDataSource<Set<ApiDefinition>>(redisTemplate, ruleKeys,
				sources -> ArrayUtils.isEmpty(sources) ? Collections.emptySet() : parseApiDefinitionJsons(sources));
		GatewayApiDefinitionManager.register2Property(api.getProperty());
		sentinelRuleUpdateEventListenerContainer.subscribe(name, api);
	}

	private Set<GatewayFlowRule> parseGatewayFlowRuleJsons(String[] sources) {
		Set<GatewayFlowRule> set = new HashSet<>();
		Arrays.stream(sources).forEach(source -> {
			Set<GatewayFlowRule> rules = JacksonUtils.parseJson(source, new TypeReference<Set<GatewayFlowRule>>() {
			});
			if (rules != null) {
				set.addAll(rules);
			}
		});
		return set;
	}

	private Set<ApiDefinition> parseApiDefinitionJsons(String[] sources) {
		Set<ApiDefinition> set = new HashSet<>();
		Arrays.stream(sources).forEach(source -> {
			Set<ApiDefinition> rules = JacksonUtils.parseJson(source, new TypeReference<Set<ApiDefinition>>() {
			});
			if (rules != null) {
				set.addAll(rules);
			}
		});
		return set;
	}


	@Async
	@EventListener({ SentinelRulePublishEvent.class })
	public void handleSentinelRulePublishEvent(SentinelRulePublishEvent event) {
		switch (event.getRuleType()) {
		case GATEWAY_FLOW:
			loadGatewayFlowRules(event.getRuleKeys());
			break;
		default:
			log.warn("暂不支持的规则类型：" + event.getRuleType());
			break;
		}
	}

}
