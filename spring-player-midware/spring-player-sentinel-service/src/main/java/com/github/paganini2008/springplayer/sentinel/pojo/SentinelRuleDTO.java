package com.github.paganini2008.springplayer.sentinel.pojo;

import java.util.List;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.github.paganini2008.springplayer.common.JacksonUtils;
import com.github.paganini2008.springplayer.common.sentinel.RuleType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * SentinelRuleDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Setter
@Getter
public class SentinelRuleDTO {

	private Long id;
	private String ruleKey;
	private RuleType ruleType;
	private List<AuthorityRule> authority;
	private List<DegradeRule> degrade;
	private List<SystemRule> system;
	private List<FlowRule> flow;
	private List<ParamFlowRule> paramFlow;

	public String getRule() {
		String json = "";
		switch (getRuleType()) {
		case AUTHORITY:
			json = JacksonUtils.toJsonString(getAuthority());
			break;
		case DEGRADE:
			json = JacksonUtils.toJsonString(getDegrade());
			break;
		case SYSTEM:
			json = JacksonUtils.toJsonString(getSystem());
			break;
		case FLOW:
			json = JacksonUtils.toJsonString(getFlow());
			break;
		case PARAM_FLOW:
			json = JacksonUtils.toJsonString(getParamFlow());
			break;
		default:
			break;
		}
		return json;
	}

}
