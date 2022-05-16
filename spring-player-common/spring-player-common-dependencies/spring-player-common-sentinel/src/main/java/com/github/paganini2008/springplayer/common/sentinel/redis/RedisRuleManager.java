package com.github.paganini2008.springplayer.common.sentinel.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.scheduling.annotation.Async;

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
import com.github.paganini2008.springplayer.common.sentinel.RuleManager;
import com.github.paganini2008.springplayer.common.utils.JacksonUtils;

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
	public void loadAuthorityRules(String[] ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		String name = Arrays.toString(ruleKeys);
		sentinelRuleUpdateEventListenerContainer.unsubscribe(name);
		FixedRedisDataSource<List<AuthorityRule>> authorityRuleDataSource = new FixedRedisDataSource<>(redisOperations, ruleKeys,
				sources -> ArrayUtils.isEmpty(sources) ? Collections.emptyList() : parseAuthorityRuleJsons(sources));
		AuthorityRuleManager.register2Property(authorityRuleDataSource.getProperty());
		sentinelRuleUpdateEventListenerContainer.subscribe(name, authorityRuleDataSource);

	}

	private List<AuthorityRule> parseAuthorityRuleJsons(String[] sources) {
		List<AuthorityRule> list = new ArrayList<>();
		Arrays.stream(sources).forEach(source -> {
			List<AuthorityRule> rules = JacksonUtils.parseJson(source, new TypeReference<List<AuthorityRule>>() {
			});
			if (rules != null) {
				list.addAll(rules);
			}
		});
		return list;
	}

	/**
	 * 降级规则
	 * 
	 * @param ruleKey
	 * @param channel
	 */
	public void loadDegradeRules(String[] ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		String name = Arrays.toString(ruleKeys);

		sentinelRuleUpdateEventListenerContainer.unsubscribe(name);
		FixedRedisDataSource<List<DegradeRule>> degradeRuleDataSource = new FixedRedisDataSource<>(redisOperations, ruleKeys,
				sources -> ArrayUtils.isEmpty(sources) ? Collections.emptyList() : parseDegradeRuleJsons(sources));
		DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
		sentinelRuleUpdateEventListenerContainer.subscribe(name, degradeRuleDataSource);
	}

	private List<DegradeRule> parseDegradeRuleJsons(String[] sources) {
		List<DegradeRule> list = new ArrayList<>();
		Arrays.stream(sources).forEach(source -> {
			List<DegradeRule> rules = JacksonUtils.parseJson(source, new TypeReference<List<DegradeRule>>() {
			});
			if (rules != null) {
				list.addAll(rules);
			}
		});
		return list;
	}

	/**
	 * 系统规则
	 * 
	 * @param ruleKey
	 * @param channel
	 */
	public void loadSystemRules(String[] ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		String name = Arrays.toString(ruleKeys);
		sentinelRuleUpdateEventListenerContainer.unsubscribe(name);
		FixedRedisDataSource<List<SystemRule>> systemRuleDataSource = new FixedRedisDataSource<List<SystemRule>>(redisOperations, ruleKeys,
				sources -> ArrayUtils.isEmpty(sources) ? Collections.emptyList() : parseSystemRuleJsons(sources));
		SystemRuleManager.register2Property(systemRuleDataSource.getProperty());
		sentinelRuleUpdateEventListenerContainer.subscribe(name, systemRuleDataSource);
	}

	private List<SystemRule> parseSystemRuleJsons(String[] sources) {
		List<SystemRule> list = new ArrayList<>();
		Arrays.stream(sources).forEach(source -> {
			List<SystemRule> rules = JacksonUtils.parseJson(source, new TypeReference<List<SystemRule>>() {
			});
			if (rules != null) {
				list.addAll(rules);
			}
		});
		return list;
	}

	public void loadFlowRules(String[] ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		String name = Arrays.toString(ruleKeys);
		sentinelRuleUpdateEventListenerContainer.unsubscribe(name);
		FixedRedisDataSource<List<FlowRule>> flowRuleDataSource = new FixedRedisDataSource<List<FlowRule>>(redisOperations, ruleKeys,
				sources -> ArrayUtils.isEmpty(sources) ? Collections.emptyList() : parseFlowRuleJsons(sources));
		FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
		sentinelRuleUpdateEventListenerContainer.subscribe(name, flowRuleDataSource);
	}

	private List<FlowRule> parseFlowRuleJsons(String[] sources) {
		List<FlowRule> list = new ArrayList<>();
		Arrays.stream(sources).forEach(source -> {
			List<FlowRule> rules = JacksonUtils.parseJson(source, new TypeReference<List<FlowRule>>() {
			});
			if (rules != null) {
				list.addAll(rules);
			}
		});
		return list;
	}

	public void loadParamFlowRules(String[] ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		String name = Arrays.toString(ruleKeys);
		sentinelRuleUpdateEventListenerContainer.unsubscribe(name);
		FixedRedisDataSource<List<ParamFlowRule>> flowRuleDataSource = new FixedRedisDataSource<List<ParamFlowRule>>(redisOperations,
				ruleKeys, sources -> ArrayUtils.isEmpty(sources) ? Collections.emptyList() : parseParamFlowRuleJsons(sources));
		ParamFlowRuleManager.register2Property(flowRuleDataSource.getProperty());
		sentinelRuleUpdateEventListenerContainer.subscribe(name, flowRuleDataSource);
	}

	private List<ParamFlowRule> parseParamFlowRuleJsons(String[] sources) {
		List<ParamFlowRule> list = new ArrayList<>();
		Arrays.stream(sources).forEach(source -> {
			List<ParamFlowRule> rules = JacksonUtils.parseJson(source, new TypeReference<List<ParamFlowRule>>() {
			});
			if (rules != null) {
				list.addAll(rules);
			}
		});
		return list;
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
