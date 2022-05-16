package com.github.paganini2008.springplayer.common.sentinel.apollo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;
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
import com.github.paganini2008.springplayer.common.sentinel.RuleManager;
import com.github.paganini2008.springplayer.common.utils.JacksonUtils;

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

	public static final String DEFAULT_NAMESPACE_NAME = "sentinel.properties";

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
	public void loadAuthorityRules(String[] ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		FixedApolloDataSource<List<AuthorityRule>> authorityRuleDataSource = new FixedApolloDataSource<>(DEFAULT_NAMESPACE_NAME, ruleKeys,
				sources -> ArrayUtils.isEmpty(sources) ? Collections.emptyList() : parseAuthorityRuleJsons(sources));
		AuthorityRuleManager.register2Property(authorityRuleDataSource.getProperty());

	}

	private List<AuthorityRule> parseAuthorityRuleJsons(String[] sources) {
		List<AuthorityRule> list = new ArrayList<>();
		Arrays.stream(sources).forEach(source -> {
			List<AuthorityRule> rules = JacksonUtils.parseJson(source, new TypeReference<List<AuthorityRule>>() {
			});
			if (rules != null) {
				list.addAll(rules);
			}
		});
		return list;
	}

	/**
	 * 加载授权规则
	 * 
	 * @param namespaceName
	 * @param ruleKey
	 */
	public void loadAuthorityRules(String namespaceName, String ruleKey) {
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

	}

	/**
	 * 加载降级规则
	 * 
	 * @param ruleKey
	 */
	public void loadDegradeRules(String[] ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		FixedApolloDataSource<List<DegradeRule>> degradeRuleDataSource = new FixedApolloDataSource<>(DEFAULT_NAMESPACE_NAME, ruleKeys,
				sources -> ArrayUtils.isEmpty(sources) ? Collections.emptyList() : parseDegradeRuleJsons(sources));
		DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());

	}

	private List<DegradeRule> parseDegradeRuleJsons(String[] sources) {
		List<DegradeRule> list = new ArrayList<>();
		Arrays.stream(sources).forEach(source -> {
			List<DegradeRule> rules = JacksonUtils.parseJson(source, new TypeReference<List<DegradeRule>>() {
			});
			if (rules != null) {
				list.addAll(rules);
			}
		});
		return list;
	}

	/**
	 * 加载降级规则
	 * 
	 * @param namespaceName
	 * @param ruleKey
	 */
	public void loadDegradeRules(String namespaceName, String ruleKey) {
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
	}

	/**
	 * 加载系统规则
	 * 
	 * @param ruleKey
	 */
	public void loadSystemRules(String[] ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		FixedApolloDataSource<List<SystemRule>> degradeRuleDataSource = new FixedApolloDataSource<>(DEFAULT_NAMESPACE_NAME, ruleKeys,
				sources -> ArrayUtils.isEmpty(sources) ? Collections.emptyList() : parseSystemRuleJsons(sources));
		SystemRuleManager.register2Property(degradeRuleDataSource.getProperty());

	}

	private List<SystemRule> parseSystemRuleJsons(String[] sources) {
		List<SystemRule> list = new ArrayList<>();
		Arrays.stream(sources).forEach(source -> {
			List<SystemRule> rules = JacksonUtils.parseJson(source, new TypeReference<List<SystemRule>>() {
			});
			if (rules != null) {
				list.addAll(rules);
			}
		});
		return list;
	}

	/**
	 * 加载系统规则
	 * 
	 * @param namespaceName
	 * @param ruleKey
	 */
	public void loadSystemRules(String namespaceName, String ruleKey) {
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
	}

	/**
	 * 加载流控规则
	 * 
	 * @param ruleKey
	 */
	public void loadFlowRules(String[] ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		FixedApolloDataSource<List<FlowRule>> flowRuleDataSource = new FixedApolloDataSource<>(DEFAULT_NAMESPACE_NAME, ruleKeys,
				sources -> ArrayUtils.isEmpty(sources) ? Collections.emptyList() : parseFlowRuleJsons(sources));
		FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
	}

	private List<FlowRule> parseFlowRuleJsons(String[] sources) {
		List<FlowRule> list = new ArrayList<>();
		Arrays.stream(sources).forEach(source -> {
			List<FlowRule> rules = JacksonUtils.parseJson(source, new TypeReference<List<FlowRule>>() {
			});
			if (rules != null) {
				list.addAll(rules);
			}
		});
		return list;
	}

	/**
	 * 加载流控规则
	 * 
	 * @param namespaceName
	 * @param ruleKey
	 */
	public void loadFlowRules(String namespaceName, String ruleKey) {
		ApolloDataSource<List<FlowRule>> flowRuleDataSource = new ApolloDataSource<List<FlowRule>>(namespaceName, ruleKey, "", source -> {
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
	}

	public void loadParamFlowRules(String[] ruleKeys) {
		if (ArrayUtils.isEmpty(ruleKeys)) {
			return;
		}
		FixedApolloDataSource<List<ParamFlowRule>> flowRuleDataSource = new FixedApolloDataSource<>(DEFAULT_NAMESPACE_NAME, ruleKeys,
				sources -> ArrayUtils.isEmpty(sources) ? Collections.emptyList() : parseParamFlowRuleJsons(sources));
		ParamFlowRuleManager.register2Property(flowRuleDataSource.getProperty());
	}

	private List<ParamFlowRule> parseParamFlowRuleJsons(String[] sources) {
		List<ParamFlowRule> list = new ArrayList<>();
		Arrays.stream(sources).forEach(source -> {
			List<ParamFlowRule> rules = JacksonUtils.parseJson(source, new TypeReference<List<ParamFlowRule>>() {
			});
			if (rules != null) {
				list.addAll(rules);
			}
		});
		return list;
	}

	/**
	 * 加载热点参数规则
	 * 
	 * @param namespaceName
	 * @param ruleKey
	 */
	public void loadParamFlowRules(String namespaceName, String ruleKey) {
		ApolloDataSource<List<ParamFlowRule>> flowRuleDataSource = new ApolloDataSource<List<ParamFlowRule>>(namespaceName, ruleKey, "",
				source -> StringUtils.isEmpty(source) ? Collections.emptyList()
						: JacksonUtils.parseJson(source, new TypeReference<List<ParamFlowRule>>() {
						}));
		ApolloDataSource<List<ParamFlowRule>> previous = paramFlowRuleCache.put(new RuleKey(namespaceName, ruleKey), flowRuleDataSource);
		if (previous != null) {
			try {
				previous.close();
			} catch (Exception ignored) {
			}
		}
		ParamFlowRuleManager.register2Property(flowRuleDataSource.getProperty());
	}

}
