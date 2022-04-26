package com.alibaba.csp.sentinel.dashboard.repository.rule.jdbc;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * RuleTypeEnums
 *
 * @author Feng Yan
 *
 * @version 2.0.5
 */
public enum RuleTypeEnums {

	// 未知规则
	UNKOWN(0, "未知规则"),

	// 流控规则
	FLOW_RULE(1, "流控规则"),

	// 降级规则
	DEGRADE_RULE(2, "降级规则"),

	// 热点规则
	PARAM_RULE(3, "热点规则"),

	// 系统规则
	SYSTEM_RULE(4, "系统规则"),

	// 授权规则
	AUTHORITY_RULE(5, "授权规则"),

	// 网关API定义
	GATEWAY_API(6, "网关API定义"),

	// 网关流控规则
	GATEWAY_FLOW_RULE(7, "网关流控规则"),

	// 集群配置
	CLUSTER_APP(8, "集群配置"),

	// 集群节点
	CLUSTER_NODE(9, "集群节点");

	private int code;
	private String name;

	private RuleTypeEnums(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return this.code;
	}

	public String getName() {
		return name;
	}

	private static final Map<Integer, RuleTypeEnums> cache = new HashMap<Integer, RuleTypeEnums>();

	static {
		for (RuleTypeEnums en : RuleTypeEnums.values()) {
			cache.put(en.getCode(), en);
		}
	}

	public static RuleTypeEnums valueOf(int code) {
		RuleTypeEnums ruleType = cache.get(code);
		return ruleType != null ? ruleType : UNKOWN;
	}

}
