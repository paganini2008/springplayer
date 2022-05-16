package com.github.paganini2008.springplayer.common.sentinel.apollo;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

/**
 * 
 * FixedApolloDataSource
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class FixedApolloDataSource<T> extends AbstractDataSource<String[], T> {

	private final Config config;
	private final String[] ruleKeys;

	private ConfigChangeListener configChangeListener;

	public FixedApolloDataSource(String namespaceName, String[] ruleKeys, Converter<String[], T> parser) {
		super(parser);

		Preconditions.checkArgument(StringUtils.isNotBlank(namespaceName), "Namespace name could not be null or empty");
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(ruleKeys), "RuleKey could not be null or empty!");

		this.ruleKeys = ruleKeys;

		this.config = ConfigService.getConfig(namespaceName);

		initialize();

		RecordLog.info("Initialized rule for namespace: {}, rule key: {}", namespaceName, Arrays.toString(ruleKeys));
	}

	private void initialize() {
		initializeConfigChangeListener();
		loadAndUpdateRules();
	}

	private void loadAndUpdateRules() {
		try {
			T newValue = loadConfig();
			if (newValue == null) {
				RecordLog.warn("[ApolloDataSource] WARN: rule config is null, you may have to check your data source");
			}
			getProperty().updateValue(newValue);
		} catch (Throwable ex) {
			RecordLog.warn("[ApolloDataSource] Error when loading rule config", ex);
		}
	}

	private void initializeConfigChangeListener() {
		configChangeListener = new ConfigChangeListener() {
			@Override
			public void onChange(ConfigChangeEvent changeEvent) {
				ConfigChange change;
				for (String changedKey : changeEvent.changedKeys()) {
					change = changeEvent.getChange(changedKey);
					if (change != null) {
						RecordLog.info("[ApolloDataSource] Received config changes: {}", change);
					}
				}
				loadAndUpdateRules();
			}
		};
		config.addChangeListener(configChangeListener, Sets.newHashSet(ruleKeys));
	}

	@Override
	public String[] readSource() throws Exception {
		return Arrays.stream(ruleKeys).map(ruleKey -> config.getProperty(ruleKey, ""))
				.filter(ruleValue -> StringUtils.isNotBlank(ruleValue)).toArray(len -> new String[len]);
	}

	@Override
	public void close() throws Exception {
		config.removeChangeListener(configChangeListener);
	}

}
