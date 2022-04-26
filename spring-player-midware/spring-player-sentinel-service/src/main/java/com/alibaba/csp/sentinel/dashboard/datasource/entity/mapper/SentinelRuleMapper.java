package com.alibaba.csp.sentinel.dashboard.datasource.entity.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.SentinelRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 
 * SentinelRuleMapper
 *
 * @author Feng Yan
 *
 * @version 2.0.5
 */
@Mapper
public interface SentinelRuleMapper extends BaseMapper<SentinelRule> {
	
	void clear(Integer ruleType);
	
}
