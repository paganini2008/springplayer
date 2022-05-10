/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.repository.rule.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.repository.rule.RuleRepository;
import com.alibaba.csp.sentinel.util.AssertUtil;

/**
 * 
 * 支持Redis的方式存储规则配置
 *
 * @author Fred Feng
 *
 * @version 2.0.5
 */
@SuppressWarnings("all")
public abstract class RedisRuleRepositorySupport<T extends RuleEntity> implements RuleRepository<T, Long> {

	private final Class<T> ruleTypeClass;
	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisAtomicLong idGen;

	public RedisRuleRepositorySupport(Class<T> ruleTypeClass, RedisTemplate<String, Object> redisTemplate, RedisAtomicLong idGen) {
		this.ruleTypeClass = ruleTypeClass;
		this.redisTemplate = redisTemplate;
		this.idGen = idGen;
	}

	@Value("${spring.application.name}")
	private String applicationName;

	public int getRuleType() {
		if (ruleTypeClass == FlowRuleEntity.class) {
			return 1;
		} else if (ruleTypeClass == DegradeRuleEntity.class) {
			return 2;
		} else if (ruleTypeClass == ParamFlowRuleEntity.class) {
			return 3;
		} else if (ruleTypeClass == SystemRuleEntity.class) {
			return 4;
		} else if (ruleTypeClass == AuthorityRuleEntity.class) {
			return 5;
		} else if (ruleTypeClass == ApiDefinitionEntity.class) {
			return 6;
		} else if (ruleTypeClass == GatewayFlowRuleEntity.class) {
			return 7;
		}
		return 0;
	}

	private String getKey() {
		return "sentinel-dashboard-cluster:" + applicationName + ":" + getRuleType();
	}

	@Override
	public T save(T entity) {
		if (entity.getId() == null) {
			entity.setId(idGen.incrementAndGet());
		}
		T processedEntity = preProcess(entity);
		if (processedEntity != null) {
			redisTemplate.opsForHash().putIfAbsent(getKey(), processedEntity.getId(), processedEntity);
		}

		return processedEntity;
	}

	@Override
	public List<T> saveAll(List<T> rules) {
		clearAll();

		if (rules == null) {
			return null;
		}
		List<T> savedRules = new ArrayList<>(rules.size());
		for (T rule : rules) {
			savedRules.add(save(rule));
		}
		return savedRules;
	}

	@Override
	public T delete(Long id) {
		T entity = (T) redisTemplate.opsForHash().get(getKey(), id);
		if (entity != null) {
			redisTemplate.opsForHash().delete(getKey(), id);
		}
		return entity;
	}

	@Override
	public T findById(Long id) {
		return (T) redisTemplate.opsForHash().get(getKey(), id);
	}

	@Override
	public List<T> findAllByMachine(MachineInfo machineInfo) {
		AssertUtil.notNull(machineInfo, "MachineInfo cannot be null");
		Map<Object, Object> entries = redisTemplate.opsForHash().entries(getKey());
		return entries
				.values().stream().filter(obj -> ((T) obj).getApp().equals(machineInfo.getApp())
						&& ((T) obj).getIp().equals(machineInfo.getIp()) && ((T) obj).getPort().equals(machineInfo.getPort()))
				.map(obj -> (T) obj).collect(Collectors.toList());
	}

	@Override
	public List<T> findAllByApp(String appName) {
		AssertUtil.notEmpty(appName, "AppName cannot be empty");
		Map<Object, Object> entries = redisTemplate.opsForHash().entries(getKey());
		return entries.values().stream().filter(obj -> ((T) obj).getApp().equals(appName)).map(obj -> (T) obj).collect(Collectors.toList());
	}

	public void clearAll() {
		redisTemplate.delete(getKey());
	}

	protected T preProcess(T entity) {
		return entity;
	}

}
