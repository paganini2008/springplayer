package com.alibaba.csp.sentinel.dashboard.repository.rule.jdbc;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.SentinelRule;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 
 * SentinelRuleService
 *
 * @author Fred Feng
 *
 * @version 2.0.5
 */
public interface SentinelRuleService extends IService<SentinelRule> {

	void clear(int ruleType);
	
}
