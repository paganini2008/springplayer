package com.github.paganini2008.springplayer.common.id.api;

import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.github.paganini2008.devtools.CharsetUtils;
import com.github.paganini2008.springplayer.common.id.IdGenerator;
import com.github.paganini2008.springplayer.common.id.IdGeneratorFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * SnowFlakeIdGeneratorFactory
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@SuppressWarnings("rawtypes")
@Slf4j
public class SnowFlakeIdGeneratorFactory implements IdGeneratorFactory, ApplicationListener<RedisKeyExpiredEvent> {

	private static final int DEFAULT_EXPIRATION_TIME = 24;
	private static final int MAX_SLOT = 31;
	private static final int MAX_CLUSTER_NODES = MAX_SLOT * MAX_SLOT;
	private static final String REDIS_KEY_PATTERN = "%s:snowflake:app:%s:%s";

	private final String namespace;
	private final StringRedisTemplate redisTemplate;
	private final RedisAtomicLong datacenterIdGen;
	private final RedisAtomicLong workerIdGen;

	public SnowFlakeIdGeneratorFactory(String namespace, RedisConnectionFactory redisConnectionFactory) {
		this.namespace = namespace;
		this.redisTemplate = new StringRedisTemplate(redisConnectionFactory);
		this.datacenterIdGen = new RedisAtomicLong(namespace + ":snowflake:datacenterId", redisConnectionFactory);
		this.workerIdGen = new RedisAtomicLong(namespace + ":snowflake:workerId", redisConnectionFactory);
	}

	private long datacenterId;
	private long workerId;

	private int sizeOfNamespace() {
		return redisTemplate.keys(namespace + ":snowflake:app:*").size();
	}

	@Override
	public IdGenerator getObject() {
		String key;
		long workerId, datacenterId;
		do {
			if (sizeOfNamespace() >= MAX_CLUSTER_NODES) {
				throw new IllegalStateException("Exceeding max cluster nodes: " + MAX_CLUSTER_NODES);
			}
			workerId = workerIdGen.getAndIncrement();
			if (workerId >= MAX_SLOT) {
				workerIdGen.set(0);
				datacenterIdGen.getAndIncrement();
			}
			datacenterId = datacenterIdGen.get();
			if (datacenterId >= MAX_SLOT) {
				datacenterIdGen.set(0);
			}
			key = String.format(REDIS_KEY_PATTERN, namespace, datacenterId, workerId);
		} while (redisTemplate.hasKey(key));

		redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()), DEFAULT_EXPIRATION_TIME, TimeUnit.HOURS);
		if (log.isInfoEnabled()) {
			log.info("Register datacenterId: {}, workerId: {}", datacenterId, workerId);
		}
		this.datacenterId = datacenterId;
		this.workerId = workerId;
		return new SnowFlakeIdGenerator(workerId, datacenterId);
	}

	@Override
	public Class<?> getObjectType() {
		return SnowFlakeIdGenerator.class;
	}

	@Override
	public void onApplicationEvent(RedisKeyExpiredEvent event) {
		String expiredKey = new String(event.getSource(), CharsetUtils.UTF_8);
		String key = String.format(REDIS_KEY_PATTERN, namespace, datacenterId, workerId);
		if (key.equals(expiredKey)) {
			if (redisTemplate.hasKey(key)) {
				log.warn("Notice that datacenterId '{}' and workerId '{}' has been used currently.", datacenterId, workerId);
			}
			redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()), DEFAULT_EXPIRATION_TIME, TimeUnit.HOURS);
			if (log.isTraceEnabled()) {
				log.trace("Renew datacenterId '{}', workerId '{}'", datacenterId, workerId);
			}
		}
	}

}
