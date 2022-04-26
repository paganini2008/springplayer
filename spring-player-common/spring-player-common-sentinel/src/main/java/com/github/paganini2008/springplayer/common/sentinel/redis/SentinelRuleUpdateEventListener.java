package com.github.paganini2008.springplayer.common.sentinel.redis;

import org.springframework.context.ApplicationListener;

/**
 * 
 * SentinelRuleUpdateEventListener
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public interface SentinelRuleUpdateEventListener extends ApplicationListener<SentinelRuleUpdateEvent>{

	String[] getRuleKeys();
	
}
