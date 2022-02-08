package com.github.paganini2008.springplayer.gateway.sentinel.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.gateway.sentinel.model.SentinelRule;
import com.github.paganini2008.springplayer.gateway.sentinel.pojo.SentinelRuleDTO;
import com.github.paganini2008.springplayer.id.IdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * SentinelRuleManagerService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SentinelRuleManagerService {

	private final SentinelRuleService sentinelRuleService;

	private final IdGenerator idGenerator;

	@Value("${spring.profiles.active:default}")
	private String env;

	public long saveSentinelRule(SentinelRuleDTO dto) {
		SentinelRule sentinelRule = null;
		if (dto.getId() != null) {
			sentinelRule = getSentinelRuleById(dto.getId());
		}
		if (StringUtils.isNotBlank(dto.getRuleKey())) {
			sentinelRule = getSentinelRuleByKey(dto.getRuleKey());
		}
		if (sentinelRule == null) {
			sentinelRule = new SentinelRule();
			sentinelRule.setId(idGenerator.generateId());
			sentinelRule.setCreateTime(LocalDateTime.now());
		}
		sentinelRule.setEnv(env);
		sentinelRule.setRuleType(dto.getRuleType());
		sentinelRule.setRuleKey(dto.getRuleKey());
		sentinelRule.setRule(dto.getRule());
		sentinelRule.setUpdateTime(LocalDateTime.now());
		sentinelRuleService.saveOrUpdate(sentinelRule);
		return sentinelRule.getId();
	}

	public SentinelRule getSentinelRuleById(Long id) {
		try {
			return sentinelRuleService.getById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public SentinelRule getSentinelRuleByKey(String ruleKey) {
		LambdaQueryWrapper<SentinelRule> query = Wrappers.<SentinelRule>lambdaQuery().eq(SentinelRule::getEnv, env)
				.eq(SentinelRule::getRuleKey, ruleKey);
		return sentinelRuleService.getOne(query);
	}

	public boolean deleteSentinelRuleById(Long id) {
		return sentinelRuleService.removeById(id);
	}

	public List<SentinelRule> findSentinelRule() {
		LambdaQueryWrapper<SentinelRule> query = Wrappers.<SentinelRule>lambdaQuery().eq(SentinelRule::getEnv, env)
				.orderByDesc(SentinelRule::getUpdateTime);
		return sentinelRuleService.list(query);
	}

}
