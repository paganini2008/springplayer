package com.github.paganini2008.springplayer.sentinel.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.github.paganini2008.springplayer.mybatis.EntityMapper;
import com.github.paganini2008.springplayer.sentinel.model.SentinelRule;

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
