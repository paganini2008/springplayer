package com.github.paganini2008.springplayer.gateway.sentinel.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paganini2008.springplayer.gateway.sentinel.mapper.SentinelRuleMapper;
import com.github.paganini2008.springplayer.gateway.sentinel.model.SentinelRule;

/**
 * 
 * SentinelRuleServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class SentinelRuleServiceImpl extends ServiceImpl<SentinelRuleMapper, SentinelRule> implements SentinelRuleService {
}
