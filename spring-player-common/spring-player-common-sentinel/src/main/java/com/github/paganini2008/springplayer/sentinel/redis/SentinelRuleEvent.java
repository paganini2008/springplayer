package com.github.paganini2008.springplayer.sentinel.redis;

import org.springframework.context.ApplicationEvent;

import com.github.paganini2008.springplayer.sentinel.RuleType;

/**
 * 
 * SentinelRuleEvent
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public abstract class SentinelRuleEvent extends ApplicationEvent {

	private static final long serialVersionUID = 239536785627535174L;

	public SentinelRuleEvent(Object source, RuleType ruleType) {
		super(source);
		this.ruleType = ruleType;
	}

	private final RuleType ruleType;

	public RuleType getRuleType() {
		return ruleType;
	}

}