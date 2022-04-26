package com.github.paganini2008.springplayer.common.sentinel.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paganini2008.springplayer.common.sentinel.RuleManager;

/**
 * 
 * SentinelExampleAutoConfig
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@ConditionalOnProperty("yl.platform.sentinel.example.enabled")
public class SentinelExampleAutoConfig {

	/**
	 * 初始化授权规则
	 */
	@PostConstruct
	public void initializeAuthorityRules() {
		List<AuthorityRule> list = new ArrayList<>();
		AuthorityRule rule = new AuthorityRule();
		rule.setStrategy(RuleConstant.AUTHORITY_WHITE);
		rule.setResource("selectOrderList");
		rule.setLimitApp("app1,app2");
		list.add(rule);

		rule = new AuthorityRule();
		rule.setStrategy(RuleConstant.AUTHORITY_BLACK);
		rule.setResource("selectProductList");
		rule.setLimitApp("app3");
		list.add(rule);
		RuleManager.loadAuthorityRules(list);
	}

	/**
	 * 初始化系统规则
	 */
	@PostConstruct
	public void initializeSystemRule() {
		SystemRule systemRule = new SystemRule();
		systemRule.setHighestCpuUsage(0.6d);
		systemRule.setHighestSystemLoad(5d);
		systemRule.setAvgRt(3000L);
		systemRule.setQps(100);
		systemRule.setMaxThread(10);
		RuleManager.loadSystemRules(Arrays.asList(systemRule));
	}

	/**
	 * 初始化降级规则
	 */
	@PostConstruct
	public void initializeDegradeRules() {
		List<DegradeRule> degradeRules = new ArrayList<>();
		degradeRules
				.add(new DegradeRule("compareName").setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT).setCount(10).setTimeWindow(10));
		degradeRules.add(new DegradeRule("testSlowQuery").setGrade(RuleConstant.DEGRADE_GRADE_RT).setCount(3000L).setTimeWindow(20));
		degradeRules
				.add(new DegradeRule("testNumber").setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO).setCount(0.2d).setTimeWindow(10));
		RuleManager.loadDegradeRules(degradeRules);
	}

	/**
	 * 初始化热点参数规则
	 * <p>
	 * 参考<a href=
	 * "https://github.com/alibaba/Sentinel/wiki/%E7%83%AD%E7%82%B9%E5%8F%82%E6%95%B0%E9%99%90%E6%B5%81">热点参数限流</a>
	 * </p>
	 */
	@PostConstruct
	public void initializeParamFlowRules() {
		ParamFlowRule paramFlowRule = new ParamFlowRule("testHotProduct").setParamIdx(0).setGrade(RuleConstant.FLOW_GRADE_QPS).setCount(5);
		RuleManager.loadParamFlowRules(Arrays.asList(paramFlowRule));
	}

	/**
	 * 初始化流控规则
	 */
	@PostConstruct
	public void initializeFlowRules() {

		List<FlowRule> flowRules = new ArrayList<>();
		flowRules.add(new FlowRule("sayHello").setGrade(RuleConstant.FLOW_GRADE_QPS).setCount(5));
		flowRules.add(new FlowRule("compareName").setGrade(RuleConstant.FLOW_GRADE_QPS).setCount(10));
		flowRules.add(new FlowRule("waitForLongTime").setGrade(RuleConstant.FLOW_GRADE_THREAD).setCount(10));
		RuleManager.loadFlowRules(flowRules);

	}

	@ComponentScan("com.yl.platform.common.sentinel.example")
	@Configuration
	public static class EmbeddedConfig {
	}

	public static void main(String[] args) throws Exception {
		List<DegradeRule> degradeRules = new ArrayList<>();
		degradeRules
				.add(new DegradeRule("compareName").setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT).setCount(10).setTimeWindow(10));
		degradeRules.add(new DegradeRule("testSlowQuery").setGrade(RuleConstant.DEGRADE_GRADE_RT).setCount(3000L).setTimeWindow(20));
		degradeRules
				.add(new DegradeRule("testNumber").setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO).setCount(0.2d).setTimeWindow(10));
		System.out.println(new ObjectMapper().writeValueAsString(degradeRules));
	}

}
