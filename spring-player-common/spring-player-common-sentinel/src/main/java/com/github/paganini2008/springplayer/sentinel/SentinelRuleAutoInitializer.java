package com.github.paganini2008.springplayer.sentinel;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.github.paganini2008.devtools.collection.CollectionUtils;

import lombok.RequiredArgsConstructor;

/**
 * 
 * SentinelRuleAutoInitializer
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class SentinelRuleAutoInitializer implements InitializingBean {

	private final List<String> ruleKeys;
	private final RuleType ruleType;
	private final RuleManager ruleManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (CollectionUtils.isEmpty(ruleKeys)) {
			return;
		}
		String[] ruleKeyArray = ruleKeys.toArray(new String[0]);
		switch (ruleType) {
		case AUTHORITY:
			ruleManager.loadAuthorityRules(ruleKeyArray);
			break;
		case DEGRADE:
			ruleManager.loadDegradeRules(ruleKeyArray);
			break;
		case SYSTEM:
			ruleManager.loadSystemRules(ruleKeyArray);
			break;
		case FLOW:
			ruleManager.loadFlowRules(ruleKeyArray);
			break;
		case PARAM_FLOW:
			ruleManager.loadParamFlowRules(ruleKeyArray);
			break;
		default:
			break;
		}

	}

	public List<String> getRuleKeys() {
		return ruleKeys;
	}

	public RuleType getRuleType() {
		return ruleType;
	}

}
