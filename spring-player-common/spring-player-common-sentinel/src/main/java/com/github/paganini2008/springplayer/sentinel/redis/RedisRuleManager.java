package com.github.paganini2008.springplayer.sentinel.redis;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.paganini2008.springplayer.common.JacksonUtils;
import com.github.paganini2008.springplayer.sentinel.RuleManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * RedisRuleManager
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class RedisRuleManager extends RuleManager {

	private final RedisOperations<String, Object> redisOperations;

	private final SentinelRuleUpdateEventListenerContainer sentinelRuleUpdateEventListenerContainer;

	/**
	 * 授权规则
	 * 
	 * @param ruleKey
	 * @param channel
	 */
	public void loadAuthorityRules(String... ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		Arrays.stream(ruleKeys).forEach(ruleKey -> {
			sentinelRuleUpdateEventListenerContainer.unsubscribe(ruleKey);
			RedisDataSource<List<AuthorityRule>> authorityRuleDataSource = new RedisDataSource<>(redisOperations, ruleKey,
					source -> StringUtils.isEmpty(source) ? Collections.emptyList()
							: JacksonUtils.parseJson(source, new TypeReference<List<AuthorityRule>>() {
							}));

			AuthorityRuleManager.register2Property(authorityRuleDataSource.getProperty());
			sentinelRuleUpdateEventListenerContainer.subscribe(ruleKey, authorityRuleDataSource);
		});

	}

	/**
	 * 降级规则
	 * 
	 * @param ruleKey
	 * @param channel
	 */
	public void loadDegradeRules(String... ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		Arrays.stream(ruleKeys).forEach(ruleKey -> {
			sentinelRuleUpdateEventListenerContainer.unsubscribe(ruleKey);
			RedisDataSource<List<DegradeRule>> degradeRuleDataSource = new RedisDataSource<>(redisOperations, ruleKey,
					source -> StringUtils.isEmpty(source) ? Collections.emptyList()
							: JacksonUtils.parseJson(source, new TypeReference<List<DegradeRule>>() {
							}));
			DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
			sentinelRuleUpdateEventListenerContainer.subscribe(ruleKey, degradeRuleDataSource);
		});
	}

	/**
	 * 系统规则
	 * 
	 * @param ruleKey
	 * @param channel
	 */
	public void loadSystemRules(String... ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		Arrays.stream(ruleKeys).forEach(ruleKey -> {
			sentinelRuleUpdateEventListenerContainer.unsubscribe(ruleKey);
			RedisDataSource<List<SystemRule>> systemRuleDataSource = new RedisDataSource<List<SystemRule>>(redisOperations, ruleKey,
					source -> StringUtils.isEmpty(source) ? Collections.emptyList()
							: JacksonUtils.parseJson(source, new TypeReference<List<SystemRule>>() {
							}));
			SystemRuleManager.register2Property(systemRuleDataSource.getProperty());
			sentinelRuleUpdateEventListenerContainer.subscribe(ruleKey, systemRuleDataSource);
		});
	}

	/**
	 * 流控规则
	 * 
	 * @param ruleKey
	 * @param channel
	 */
	public void loadFlowRules(String... ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		Arrays.stream(ruleKeys).forEach(ruleKey -> {
			sentinelRuleUpdateEventListenerContainer.unsubscribe(ruleKey);
			RedisDataSource<List<FlowRule>> flowRuleDataSource = new RedisDataSource<List<FlowRule>>(redisOperations, ruleKey,
					source -> StringUtils.isEmpty(source) ? Collections.emptyList()
							: JacksonUtils.parseJson(source, new TypeReference<List<FlowRule>>() {
							}));
			FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
			sentinelRuleUpdateEventListenerContainer.subscribe(ruleKey, flowRuleDataSource);
		});
	}

	/**
	 * 热点参数规则
	 * 
	 * @param ruleKey
	 * @param channel
	 */
	public void loadParamFlowRules(String... ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		Arrays.stream(ruleKeys).forEach(ruleKey -> {
			sentinelRuleUpdateEventListenerContainer.unsubscribe(ruleKey);
			RedisDataSource<List<ParamFlowRule>> flowRuleDataSource = new RedisDataSource<List<ParamFlowRule>>(redisOperations, ruleKey,
					source -> StringUtils.isEmpty(source) ? Collections.emptyList()
							: JacksonUtils.parseJson(source, new TypeReference<List<ParamFlowRule>>() {
							}));
			ParamFlowRuleManager.register2Property(flowRuleDataSource.getProperty());
			sentinelRuleUpdateEventListenerContainer.subscribe(ruleKey, flowRuleDataSource);
		});
	}

	@Async
	@EventListener({ SentinelRulePublishEvent.class })
	public void handleSentinelRulePublishEvent(SentinelRulePublishEvent event) {
		log.info("Publish ruleKeys: {}, ruleType: {}", Arrays.toString(event.getRuleKeys()), event.getRuleType());
		switch (event.getRuleType()) {
		case AUTHORITY:
			loadAuthorityRules(event.getRuleKeys());
			break;
		case DEGRADE:
			loadDegradeRules(event.getRuleKeys());
			break;
		case SYSTEM:
			loadSystemRules(event.getRuleKeys());
			break;
		case FLOW:
			loadFlowRules(event.getRuleKeys());
			break;
		case PARAM_FLOW:
			loadParamFlowRules(event.getRuleKeys());
			break;
		default:
			break;
		}
	}

}
