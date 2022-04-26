package com.github.paganini2008.springplayer.common.sentinel.redis;

import com.github.paganini2008.springplayer.common.sentinel.RuleType;

/**
 * 
 * SentinelRuleUpdateEvent
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public class SentinelRuleUpdateEvent extends SentinelRuleEvent {

	private static final long serialVersionUID = 4609953474074256889L;

	public SentinelRuleUpdateEvent(Object source, RuleType ruleType, String[] ruleKeys) {
		super(source, ruleType);
		this.ruleKeys = ruleKeys;
	}

	private final String[] ruleKeys;

	public String[] getRuleKeys() {
		return ruleKeys;
	}

}
