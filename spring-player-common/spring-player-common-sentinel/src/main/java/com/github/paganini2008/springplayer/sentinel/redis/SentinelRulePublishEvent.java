package com.github.paganini2008.springplayer.sentinel.redis;

import com.github.paganini2008.springplayer.sentinel.RuleType;

/**
 * 
 * SentinelRulePublishEvent
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class SentinelRulePublishEvent extends SentinelRuleEvent {

	private static final long serialVersionUID = -1895553403845556212L;

	public SentinelRulePublishEvent(Object source, RuleType ruleType, String ruleKey) {
		this(source, ruleType, new String[] { ruleKey });
	}

	public SentinelRulePublishEvent(Object source, RuleType ruleType, String[] ruleKeys) {
		super(source, ruleType);
		this.ruleKeys = ruleKeys;
	}

	private final String[] ruleKeys;

	public String[] getRuleKeys() {
		return ruleKeys;
	}

}
