package com.github.paganini2008.springplayer.sentinel.redis;

import com.github.paganini2008.springplayer.sentinel.RuleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * SentinelRuleKeys
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SentinelRuleKeys {

	private RuleType ruleType;
	private String[] ruleKeys;

	public SentinelRuleKeys(RuleType ruleType, String ruleKey) {
		this(ruleType, new String[] { ruleKey });
	}

}
