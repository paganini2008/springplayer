package com.github.paganini2008.springplayer.common.sentinel;

import java.util.List;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;

/**
 * 
 * RuleManager
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class RuleManager {

	public static void loadAuthorityRules(List<AuthorityRule> rules) {
		AuthorityRuleManager.loadRules(rules);
	}

	public static List<AuthorityRule> getAuthorityRules() {
		return AuthorityRuleManager.getRules();
	}

	public static void loadDegradeRules(List<DegradeRule> rules) {
		DegradeRuleManager.loadRules(rules);
	}

	public static List<DegradeRule> getDegradeRules() {
		return DegradeRuleManager.getRules();
	}

	public static void loadSystemRules(List<SystemRule> rules) {
		SystemRuleManager.loadRules(rules);
	}

	public static List<SystemRule> getSystemRules() {
		return SystemRuleManager.getRules();
	}

	public static void loadFlowRules(List<FlowRule> rules) {
		FlowRuleManager.loadRules(rules);
	}

	public static List<FlowRule> getFlowRules() {
		return FlowRuleManager.getRules();
	}

	public static void loadParamFlowRules(List<ParamFlowRule> rules) {
		ParamFlowRuleManager.loadRules(rules);
	}

	public static List<ParamFlowRule> getParamFlowRules() {
		return ParamFlowRuleManager.getRules();
	}

	/**
	 * 加载授权规则
	 * 
	 * @param ruleKeys
	 */
	public abstract void loadAuthorityRules(String[] ruleKeys);

	/**
	 * 加载降级规则
	 * 
	 * @param ruleKeys
	 */
	public abstract void loadDegradeRules(String[] ruleKeys);

	/**
	 * 加载系统规则
	 * 
	 * @param ruleKeys
	 */
	public abstract void loadSystemRules(String[] ruleKeys);

	/**
	 * 加载流控规则
	 * 
	 * @param ruleKeys
	 */
	public abstract void loadFlowRules(String[] ruleKeys);

	/**
	 * 加载热点参数规则
	 * 
	 * @param ruleKeys
	 */
	public abstract void loadParamFlowRules(String[] ruleKeys);

}
