package com.github.paganini2008.springplayer.common.sentinel.apollo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.paganini2008.springplayer.common.JacksonUtils;
import com.github.paganini2008.springplayer.common.sentinel.RuleManager;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * ApolloRuleManager
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class ApolloRuleManager extends RuleManager {

	public static final String DEFAULT_NAMESPACE_NAME = "sentinel";

	@AllArgsConstructor
	@Data
	static class RuleKey {

		private String namespaceName;
		private String ruleKey;
	}

	private final Map<RuleKey, ApolloDataSource<List<AuthorityRule>>> authorityRuleCache = new ConcurrentHashMap<>();
	private final Map<RuleKey, ApolloDataSource<List<DegradeRule>>> degradeRuleCache = new ConcurrentHashMap<>();
	private final Map<RuleKey, ApolloDataSource<List<SystemRule>>> systemRuleCache = new ConcurrentHashMap<>();
	private final Map<RuleKey, ApolloDataSource<List<FlowRule>>> flowRuleCache = new ConcurrentHashMap<>();
	private final Map<RuleKey, ApolloDataSource<List<ParamFlowRule>>> paramFlowRuleCache = new ConcurrentHashMap<>();

	/**
	 * 加载授权规则
	 * 
	 * @param ruleKey
	 */
	public void loadAuthorityRules(String... ruleKeys) {
		loadAuthorityRules(DEFAULT_NAMESPACE_NAME, ruleKeys);
	}

	/**
	 * 加载授权规则
	 * 
	 * @param namespaceName
	 * @param ruleKey
	 */
	public void loadAuthorityRules(String namespaceName, String... ruleKeys) {
		Arrays.stream(ruleKeys).forEach(ruleKey -> {
			ApolloDataSource<List<AuthorityRule>> authorityRuleDataSource = new ApolloDataSource<>(namespaceName, ruleKey, "",
					source -> StringUtils.isEmpty(source) ? Collections.emptyList()
							: JacksonUtils.parseJson(source, new TypeReference<List<AuthorityRule>>() {
							}));
			ApolloDataSource<List<AuthorityRule>> previous = authorityRuleCache.put(new RuleKey(namespaceName, ruleKey),
					authorityRuleDataSource);
			if (previous != null) {
				try {
					previous.close();
				} catch (Exception ignored) {
				}
			}
			AuthorityRuleManager.register2Property(authorityRuleDataSource.getProperty());
		});

	}

	/**
	 * 加载降级规则
	 * 
	 * @param ruleKey
	 */
	public void loadDegradeRules(String... ruleKeys) {
		loadDegradeRules(DEFAULT_NAMESPACE_NAME, ruleKeys);
	}

	/**
	 * 加载降级规则
	 * 
	 * @param namespaceName
	 * @param ruleKey
	 */
	public void loadDegradeRules(String namespaceName, String... ruleKeys) {
		Arrays.stream(ruleKeys).forEach(ruleKey -> {
			ApolloDataSource<List<DegradeRule>> degradeRuleDataSource = new ApolloDataSource<>(namespaceName, ruleKey, "",
					source -> StringUtils.isEmpty(source) ? Collections.emptyList()
							: JacksonUtils.parseJson(source, new TypeReference<List<DegradeRule>>() {
							}));
			ApolloDataSource<List<DegradeRule>> previous = degradeRuleCache.put(new RuleKey(namespaceName, ruleKey), degradeRuleDataSource);
			if (previous != null) {
				try {
					previous.close();
				} catch (Exception ignored) {
				}
			}
			DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
		});

	}

	/**
	 * 加载系统规则
	 * 
	 * @param ruleKey
	 */
	public void loadSystemRules(String... ruleKeys) {
		loadSystemRules(DEFAULT_NAMESPACE_NAME, ruleKeys);
	}

	/**
	 * 加载系统规则
	 * 
	 * @param namespaceName
	 * @param ruleKey
	 */
	public void loadSystemRules(String namespaceName, String... ruleKeys) {
		Arrays.stream(ruleKeys).forEach(ruleKey -> {
			ApolloDataSource<List<SystemRule>> systemRuleDataSource = new ApolloDataSource<List<SystemRule>>(namespaceName, ruleKey, "",
					source -> StringUtils.isEmpty(source) ? Collections.emptyList()
							: JacksonUtils.parseJson(source, new TypeReference<List<SystemRule>>() {
							}));
			ApolloDataSource<List<SystemRule>> previous = systemRuleCache.put(new RuleKey(namespaceName, ruleKey), systemRuleDataSource);
			if (previous != null) {
				try {
					previous.close();
				} catch (Exception ignored) {
				}
			}
			SystemRuleManager.register2Property(systemRuleDataSource.getProperty());
		});
	}

	/**
	 * 加载流控规则
	 * 
	 * @param ruleKey
	 */
	public void loadFlowRules(String... ruleKeys) {
		loadFlowRules(DEFAULT_NAMESPACE_NAME, ruleKeys);
	}

	/**
	 * 加载流控规则
	 * 
	 * @param namespaceName
	 * @param ruleKey
	 */
	public void loadFlowRules(String namespaceName, String... ruleKeys) {
		Arrays.stream(ruleKeys).forEach(ruleKey -> {
			ApolloDataSource<List<FlowRule>> flowRuleDataSource = new ApolloDataSource<List<FlowRule>>(namespaceName, ruleKey, "",
					source -> {
						return StringUtils.isEmpty(source) ? Collections.emptyList()
								: JacksonUtils.parseJson(source, new TypeReference<List<FlowRule>>() {
								});
					});
			ApolloDataSource<List<FlowRule>> previous = flowRuleCache.put(new RuleKey(namespaceName, ruleKey), flowRuleDataSource);
			if (previous != null) {
				try {
					previous.close();
				} catch (Exception ignored) {
				}
			}
			FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
		});
	}

	/**
	 * 加载热点参数规则
	 * 
	 * @param ruleKey
	 */
	public void loadParamFlowRules(String... ruleKeys) {
		loadParamFlowRules(DEFAULT_NAMESPACE_NAME, ruleKeys);
	}

	/**
	 * 加载热点参数规则
	 * 
	 * @param namespaceName
	 * @param ruleKey
	 */
	public void loadParamFlowRules(String namespaceName, String... ruleKeys) {
		Arrays.stream(ruleKeys).forEach(ruleKey -> {
			ApolloDataSource<List<ParamFlowRule>> flowRuleDataSource = new ApolloDataSource<List<ParamFlowRule>>(namespaceName, ruleKey, "",
					source -> StringUtils.isEmpty(source) ? Collections.emptyList()
							: JacksonUtils.parseJson(source, new TypeReference<List<ParamFlowRule>>() {
							}));
			ApolloDataSource<List<ParamFlowRule>> previous = paramFlowRuleCache.put(new RuleKey(namespaceName, ruleKey),
					flowRuleDataSource);
			if (previous != null) {
				try {
					previous.close();
				} catch (Exception ignored) {
				}
			}
			ParamFlowRuleManager.register2Property(flowRuleDataSource.getProperty());
		});

	}

}
