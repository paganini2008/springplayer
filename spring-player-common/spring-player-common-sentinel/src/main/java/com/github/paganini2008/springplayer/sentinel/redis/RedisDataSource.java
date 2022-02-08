package com.github.paganini2008.springplayer.sentinel.redis;

import org.springframework.data.redis.core.RedisOperations;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * RedisDataSource
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class RedisDataSource<T> extends AbstractDataSource<String, T> implements SentinelRuleUpdateEventListener {

	public RedisDataSource(RedisOperations<String, Object> redisOperations, String ruleKey, Converter<String, T> parser) {
		this(redisOperations, ruleKey, "rule", parser);
	}

	public RedisDataSource(RedisOperations<String, Object> redisOperations, String ruleKey, String ruleProperty,
			Converter<String, T> parser) {
		super(parser);
		this.redisOperations = redisOperations;
		this.ruleKey = ruleKey;
		this.ruleProperty = ruleProperty;
		initializeConfig();
	}

	private final RedisOperations<String, Object> redisOperations;
	private final String ruleProperty;
	private final String ruleKey;

	private void initializeConfig() {
		try {
			T newValue = loadConfig();
			getProperty().updateValue(newValue);
			log.info("Initialze gateway sentinel rule");
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
	}

	@Override
	public String readSource() throws Exception {
		return (String) redisOperations.opsForHash().get(ruleKey, ruleProperty);
	}

	@Override
	public void close() throws Exception {
	}

	@Override
	public void onApplicationEvent(SentinelRuleUpdateEvent event) {
		if (event.getRuleKey().equals(this.ruleKey)) {
			fireSentinelRuleFefreshed();
		}
	}

	private void fireSentinelRuleFefreshed() {
		T newValue;
		try {
			newValue = loadConfig();
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			newValue = null;
		}
		if (newValue != null) {
			getProperty().updateValue(newValue);
			log.info("Refresh gateway sentinel rule by redis.");
		}
	}

	public String getRuleKey() {
		return ruleKey;
	}

	public String toString() {
		return "[RedisDataSource] ruleKey: " + getRuleKey();
	}

}
