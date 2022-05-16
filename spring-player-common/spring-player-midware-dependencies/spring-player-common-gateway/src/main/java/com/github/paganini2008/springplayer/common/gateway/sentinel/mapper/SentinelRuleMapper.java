package com.github.paganini2008.springplayer.common.gateway.sentinel.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.github.paganini2008.springplayer.common.gateway.sentinel.model.SentinelRule;
import com.github.paganini2008.springplayer.common.mybatis.EntityMapper;

/**
 * 
 * SentinelRuleMapper
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Mapper
public interface SentinelRuleMapper extends EntityMapper<SentinelRule> {
}
