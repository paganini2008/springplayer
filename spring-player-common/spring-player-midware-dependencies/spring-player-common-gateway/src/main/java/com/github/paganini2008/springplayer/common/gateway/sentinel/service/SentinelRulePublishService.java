package com.github.paganini2008.springplayer.common.gateway.sentinel.service;

import static com.github.paganini2008.springplayer.common.gateway.sentinel.GatewaySentinelRuleManager.SENTINEL_GATEWAY_RULE;
import static com.github.paganini2008.springplayer.common.gateway.sentinel.GatewaySentinelRuleManager.SENTINEL_GATEWAY_RULE_API;
import static com.github.paganini2008.springplayer.common.sentinel.SentinelConstants.REDIS_PUBSUB_SENTINEL_RULE_PUBLISH;
import static com.github.paganini2008.springplayer.common.sentinel.SentinelConstants.REDIS_PUBSUB_SENTINEL_RULE_UPDATE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.paganini2008.devtools.collection.CollectionUtils;
import com.github.paganini2008.springplayer.common.gateway.sentinel.model.SentinelRule;
import com.github.paganini2008.springplayer.common.gateway.sentinel.pojo.GatewayFlowRuleDTO;
import com.github.paganini2008.springplayer.common.redis.pubsub.RedisPubSubService;
import com.github.paganini2008.springplayer.common.sentinel.RuleType;
import com.github.paganini2008.springplayer.common.sentinel.redis.SentinelRuleKeys;
import com.github.paganini2008.springplayer.common.utils.JacksonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * SentinelRulePublishService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SentinelRulePublishService {

	private final SentinelRuleService sentinelRuleService;

	private final RedisTemplate<String, Object> redisTemplate;

	private final RedisPubSubService redisPubSubService;

	@Value("${spring.profiles.active:default}")
	private String env;

	public void publishAllRuleTypes() {
		for (RuleType ruleType : RuleType.values()) {
			publishSentinelRule(ruleType);
		}
	}

	public void publishSentinelRule(RuleType ruleType) {
		LambdaQueryWrapper<SentinelRule> query = Wrappers.<SentinelRule>lambdaQuery().eq(SentinelRule::getEnv, env)
				.eq(SentinelRule::getRuleType, ruleType.getValue());
		List<SentinelRule> all = sentinelRuleService.list(query);
		if (CollectionUtils.isNotEmpty(all)) {
			all.forEach(rule -> {
				redisTemplate.opsForHash().put(rule.getRuleKey(), SENTINEL_GATEWAY_RULE,
						renderRule(rule.getRuleKey(), rule.getRuleType(), rule.getRule()));
				redisTemplate.opsForHash().put(rule.getRuleKey(), SENTINEL_GATEWAY_RULE_API,
						renderApi(rule.getRuleKey(), rule.getRuleType(), rule.getRule()));
			});
			String[] ruleKeys = all.stream().map(rule -> rule.getRuleKey()).toArray(l -> new String[l]);
			redisPubSubService.convertAndMulticast(REDIS_PUBSUB_SENTINEL_RULE_PUBLISH, new SentinelRuleKeys(ruleType, ruleKeys));
		}

	}

	private String renderApi(String ruleKey, RuleType ruleType, String rule) {
		Set<ApiDefinition> apis = new HashSet<>();
		GatewayFlowRuleDTO[] flowRules = JacksonUtils.parseJson(rule, GatewayFlowRuleDTO[].class);
		Arrays.stream(flowRules).forEach(flowRule -> {
			ApiDefinition api = new ApiDefinition(flowRule.getResource());
			Set<ApiPredicateItem> predicateItems = flowRule.getApi().stream().map(dto -> {
				ApiPathPredicateItem appi = new ApiPathPredicateItem();
				appi.setPattern(dto.getPattern());
				if (dto.getMatchStrategy() != null) {
					appi.setMatchStrategy(dto.getMatchStrategy());
				}
				return appi;
			}).collect(Collectors.toSet());
			api.setPredicateItems(predicateItems);
			apis.add(api);
		});
		return JacksonUtils.toJsonString(apis);
	}

	private String renderRule(String ruleKey, RuleType ruleType, String rule) {
		Set<GatewayFlowRule> sets = new HashSet<>();
		GatewayFlowRuleDTO[] flowRules = JacksonUtils.parseJson(rule, GatewayFlowRuleDTO[].class);
		Arrays.stream(flowRules).forEach(flowRule -> {
			GatewayFlowRule gfr = new GatewayFlowRule(flowRule.getResource());
			if (flowRule.getBurst() != null) {
				gfr.setBurst(flowRule.getBurst());
			}
			if (flowRule.getControlBehavior() != null) {
				gfr.setControlBehavior(flowRule.getControlBehavior());
			}
			if (flowRule.getCount() != null) {
				gfr.setCount(flowRule.getCount());
			}
			if (flowRule.getGrade() != null) {
				gfr.setGrade(flowRule.getGrade());
			}
			if (flowRule.getIntervalSec() != null) {
				gfr.setIntervalSec(flowRule.getIntervalSec());
			}
			if (flowRule.getMaxQueueingTimeoutMs() != null) {
				gfr.setMaxQueueingTimeoutMs(flowRule.getMaxQueueingTimeoutMs());
			}
			if (flowRule.getResourceMode() != null) {
				gfr.setResourceMode(flowRule.getResourceMode());
			}
			sets.add(gfr);
		});
		return JacksonUtils.toJsonString(sets);
	}

	public void updateSentinelRule(Long id) {
		SentinelRule rule = sentinelRuleService.getById(id);
		redisTemplate.opsForHash().put(rule.getRuleKey(), SENTINEL_GATEWAY_RULE,
				renderRule(rule.getRuleKey(), rule.getRuleType(), rule.getRule()));
		redisTemplate.opsForHash().put(rule.getRuleKey(), SENTINEL_GATEWAY_RULE_API,
				renderApi(rule.getRuleKey(), rule.getRuleType(), rule.getRule()));

		redisPubSubService.convertAndMulticast(REDIS_PUBSUB_SENTINEL_RULE_UPDATE,
				new SentinelRuleKeys(rule.getRuleType(), rule.getRuleKey()));
	}

	@Async
	@EventListener({ ContextRefreshedEvent.class })
	public void initializeRoute() {
		try {
			publishAllRuleTypes();
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		log.info("初始流控规则结束");
	}

}
