package com.github.paganini2008.springplayer.sentinel.service;

import static com.github.paganini2008.springplayer.sentinel.SentinelConstants.REDIS_PUBSUB_CHANNEL_SENTINEL_RULE_PUBLISH;
import static com.github.paganini2008.springplayer.sentinel.SentinelConstants.REDIS_PUBSUB_CHANNEL_SENTINEL_RULE_UPDATE;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.paganini2008.devtools.collection.CollectionUtils;
import com.github.paganini2008.springplayer.common.redis.pubsub.RedisPubSubService;
import com.github.paganini2008.springplayer.sentinel.RuleType;
import com.github.paganini2008.springplayer.sentinel.model.SentinelRule;
import com.github.paganini2008.springplayer.sentinel.redis.SentinelRuleKeys;

import lombok.RequiredArgsConstructor;

/**
 * 
 * SentinelRulePublishService
 *
 * @author Feng Yan
 * @version 1.0.0
 */
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
			all.forEach(sentinelRule -> {
				redisTemplate.opsForHash().put(sentinelRule.getRuleKey(), "rule", sentinelRule.getRule());
			});
			String[] ruleKeys = all.stream().map(rule -> rule.getRuleKey()).toArray(l -> new String[l]);
			redisPubSubService.convertAndMulticast(REDIS_PUBSUB_CHANNEL_SENTINEL_RULE_PUBLISH, new SentinelRuleKeys(ruleType, ruleKeys));
		}

	}


	public void updateSentinelRule(Long id) {
		SentinelRule sentinelRule = sentinelRuleService.getById(id);
		redisTemplate.opsForHash().put(sentinelRule.getRuleKey(), "rule", sentinelRule.getRule());
		redisPubSubService.convertAndMulticast(REDIS_PUBSUB_CHANNEL_SENTINEL_RULE_UPDATE,
				new SentinelRuleKeys(sentinelRule.getRuleType(), sentinelRule.getRuleKey()));
	}

}
