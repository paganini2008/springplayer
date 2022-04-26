package com.alibaba.csp.sentinel.dashboard.repository.rule.jdbc;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.SentinelRule;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.mapper.SentinelRuleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 
 * SentinelRuleServiceImpl
 *
 * @author Feng Yan
 *
 * @version 2.0.5
 */
public class SentinelRuleServiceImpl extends ServiceImpl<SentinelRuleMapper, SentinelRule> implements SentinelRuleService {

	private final SentinelRuleMapper sentinelRuleDao;

	public SentinelRuleServiceImpl(SentinelRuleMapper sentinelRuleDao) {
		this.sentinelRuleDao = sentinelRuleDao;
	}

	@Override
	public void clear(int ruleType) {
		sentinelRuleDao.clear(ruleType);
	}

}
