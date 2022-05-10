package com.alibaba.csp.sentinel.dashboard.repository.rule.jdbc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.SentinelRule;
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
import com.alibaba.csp.sentinel.dashboard.util.JacksonUtils;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 
 * 支持Jdbc的方式存储规则配置
 *
 * @author Fred Feng
 *
 * @version 2.0.5
 */
@SuppressWarnings("all")
public abstract class JdbcRuleRepositorySupport<T extends RuleEntity> implements RuleRepository<T, Long> {

	private final Class<T> ruleTypeClass;
	private final SentinelRuleService sentinelRuleService;
	private final RedisAtomicLong idGen;

	public JdbcRuleRepositorySupport(Class<T> ruleTypeClass, SentinelRuleService sentinelRuleService, RedisAtomicLong idGen) {
		this.ruleTypeClass = ruleTypeClass;
		this.sentinelRuleService = sentinelRuleService;
		this.idGen = idGen;
	}

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

	@Override
	public T save(T entity) {
		boolean updated = true;
		if (entity.getId() == null) {
			entity.setId(idGen.incrementAndGet());
			updated = false;
		}
		T processedEntity = preProcess(entity);
		if (processedEntity != null) {
			SentinelRule sentinelRule = new SentinelRule();
			sentinelRule.setId(processedEntity.getId());
			sentinelRule.setAppName(processedEntity.getApp());
			sentinelRule.setIpAddr(processedEntity.getIp());
			sentinelRule.setPort(processedEntity.getPort());
			sentinelRule.setType(getRuleType());
			sentinelRule.setContent(JacksonUtils.toJsonString(processedEntity));
			sentinelRule.setUpdatedTime(LocalDateTime.now());
			if (!updated) {
				sentinelRule.setCreatedTime(LocalDateTime.now());
			}
			sentinelRuleService.saveOrUpdate(sentinelRule);
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
		T entity = null;
		SentinelRule sentinelRule = sentinelRuleService.getById(id);
		if (sentinelRule != null) {
			entity = JacksonUtils.parseJson(sentinelRule.getContent(), ruleTypeClass);
			sentinelRuleService.removeById(id);
		}
		return entity;
	}

	@Override
	public T findById(Long id) {
		SentinelRule sentinelRule = sentinelRuleService.getById(id);
		return JacksonUtils.parseJson(sentinelRule.getContent(), ruleTypeClass);
	}

	@Override
	public List<T> findAllByMachine(MachineInfo machineInfo) {
		AssertUtil.notNull(machineInfo, "MachineInfo cannot be null");
		LambdaQueryWrapper<SentinelRule> query = new LambdaQueryWrapper<SentinelRule>().eq(SentinelRule::getAppName, machineInfo.getApp())
				.eq(SentinelRule::getIpAddr, machineInfo.getIp()).eq(SentinelRule::getPort, machineInfo.getPort())
				.eq(SentinelRule::getType, getRuleType());
		List<SentinelRule> rules = sentinelRuleService.list(query);
		if (rules != null) {
			return rules.stream().map(e -> JacksonUtils.parseJson(e.getContent(), ruleTypeClass)).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public List<T> findAllByApp(String appName) {
		AssertUtil.notEmpty(appName, "AppName cannot be empty");
		LambdaQueryWrapper<SentinelRule> query = new LambdaQueryWrapper<SentinelRule>().eq(SentinelRule::getAppName, appName)
				.eq(SentinelRule::getType, getRuleType());
		List<SentinelRule> rules = sentinelRuleService.list(query);
		if (rules != null) {
			return rules.stream().map(e -> JacksonUtils.parseJson(e.getContent(), ruleTypeClass)).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	public void clearAll() {
		sentinelRuleService.clear(getRuleType());
	}

	protected T preProcess(T entity) {
		return entity;
	}

}
