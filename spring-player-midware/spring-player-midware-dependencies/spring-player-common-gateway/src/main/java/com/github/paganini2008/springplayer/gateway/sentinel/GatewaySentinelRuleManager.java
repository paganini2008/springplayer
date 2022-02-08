package com.github.paganini2008.springplayer.gateway.sentinel;

import java.util.Arrays;
import java.util.Collections;
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
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.JacksonUtils;
import com.github.paganini2008.springplayer.sentinel.redis.RedisDataSource;
import com.github.paganini2008.springplayer.sentinel.redis.SentinelRulePublishEvent;
import com.github.paganini2008.springplayer.sentinel.redis.SentinelRuleUpdateEventListenerContainer;

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
		Arrays.stream(ruleKeys).forEach(ruleKey -> {
			String name = ruleKey + ":" + SENTINEL_GATEWAY_RULE;
			sentinelRuleUpdateEventListenerContainer.unsubscribe(name);
			RedisDataSource<Set<GatewayFlowRule>> rule = new RedisDataSource<Set<GatewayFlowRule>>(redisTemplate, ruleKey,
					SENTINEL_GATEWAY_RULE, s -> StringUtils.isEmpty(s) ? Collections.emptySet()
							: JacksonUtils.parseJson(s, new TypeReference<Set<GatewayFlowRule>>() {
							}));
			GatewayRuleManager.register2Property(rule.getProperty());
			sentinelRuleUpdateEventListenerContainer.subscribe(name, rule);

			name = ruleKey + ":" + SENTINEL_GATEWAY_RULE_API;
			sentinelRuleUpdateEventListenerContainer.unsubscribe(name);
			RedisDataSource<Set<ApiDefinition>> api = new RedisDataSource<Set<ApiDefinition>>(redisTemplate, ruleKey,
					SENTINEL_GATEWAY_RULE_API, s -> StringUtils.isEmpty(s) ? Collections.emptySet()
							: JacksonUtils.parseJson(s, new TypeReference<Set<ApiDefinition>>() {
							}));
			GatewayApiDefinitionManager.register2Property(api.getProperty());
			sentinelRuleUpdateEventListenerContainer.subscribe(name, api);
		});
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
