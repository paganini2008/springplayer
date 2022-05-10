package com.github.paganini2008.springplayer.common.sentinel.redis;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.ArrayUtils;
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

	private static final String RULE_HASH_KEY = "rule";

	public RedisDataSource(RedisOperations<String, Object> redisOperations, String ruleKey, Converter<String, T> parser) {
		super(parser);
		this.redisOperations = redisOperations;
		this.ruleKey = ruleKey;
		this.lock = new ReentrantLock();
		initializeConfig();
	}

	private final RedisOperations<String, Object> redisOperations;
	private final String ruleKey;
	private final Lock lock;

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
		return (String) redisOperations.opsForHash().get(ruleKey, RULE_HASH_KEY);
	}

	@Override
	public void close() throws Exception {
	}

	@Override
	public void onApplicationEvent(SentinelRuleUpdateEvent event) {
		if (ArrayUtils.contains(event.getRuleKeys(), this.ruleKey)) {
			lock.lock();
			try {
				fireSentinelRuleFefreshed();
			} finally {
				lock.unlock();
			}
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

	public String[] getRuleKeys() {
		return new String[] { ruleKey };
	}

	public String toString() {
		return "[RedisDataSource] ruleKeys: " + Arrays.toString(getRuleKeys());
	}

}
