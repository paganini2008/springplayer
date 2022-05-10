package com.github.paganini2008.springplayer.common.sentinel.redis;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.CollectionUtils;

import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * FixedRedisDataSource
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class FixedRedisDataSource<T> extends AbstractDataSource<String[], T> implements SentinelRuleUpdateEventListener {

	private static final String RULE_HASH_KEY = "rule";

	public FixedRedisDataSource(RedisOperations<String, Object> redisOperations, String[] ruleKeys, Converter<String[], T> parser) {
		super(parser);
		this.redisOperations = redisOperations;
		this.ruleKeys = ruleKeys;
		this.lock = new ReentrantLock();
		initializeConfig();
	}

	private final RedisOperations<String, Object> redisOperations;
	private final String[] ruleKeys;
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
	public String[] readSource() throws Exception {
		return Arrays.stream(ruleKeys).map(ruleKey -> (String) redisOperations.opsForHash().get(ruleKey, RULE_HASH_KEY))
				.filter(ruleValue -> StringUtils.isNotBlank(ruleValue)).toArray(len -> new String[len]);

	}

	@Override
	public void close() throws Exception {
	}

	@Override
	public void onApplicationEvent(SentinelRuleUpdateEvent event) {
		Collection<String> source = Arrays.asList(this.ruleKeys);
		Collection<String> canditates = Arrays.asList(event.getRuleKeys());
		if (CollectionUtils.containsAny(source, canditates)) {
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
		return ruleKeys;
	}

	public String toString() {
		return "[RedisDataSource] ruleKeys: " + Arrays.toString(getRuleKeys());
	}

}
