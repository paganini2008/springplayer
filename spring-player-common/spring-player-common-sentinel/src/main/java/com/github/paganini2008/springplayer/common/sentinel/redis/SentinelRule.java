package com.github.paganini2008.springplayer.common.sentinel.redis;

import java.util.List;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.github.paganini2008.springplayer.common.sentinel.RuleType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * SentinelRule
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@Setter
@Getter
public class SentinelRule {

	private String ruleKey;
	private RuleType ruleType;
	private List<AuthorityRule> authority;
	private List<DegradeRule> degrade;
	private List<SystemRule> system;
	private List<FlowRule> flow;
	private List<ParamFlowRule> paramFlow;

}
