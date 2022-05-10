package com.github.paganini2008.springplayer.common.sentinel.redis;

import org.springframework.context.ApplicationListener;

/**
 * 
 * SentinelRuleUpdateEventListener
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface SentinelRuleUpdateEventListener extends ApplicationListener<SentinelRuleUpdateEvent>{

	String[] getRuleKeys();
	
}
