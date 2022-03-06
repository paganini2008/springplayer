package com.github.paganini2008.springplayer.id;

import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.github.paganini2008.devtools.CharsetUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * SnowFlakeIdGeneratorFactory
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@SuppressWarnings("rawtypes")
@Slf4j
public class SnowFlakeIdGeneratorFactory implements IdGeneratorFactory, ApplicationListener<RedisKeyExpiredEvent> {

	private static final int MAX_SLOT = 31;
	private static final int MAX_CLUSTER_NODES = MAX_SLOT * MAX_SLOT;
	private static final String KEY_PATTERN = "%s:snowflake:app:%s:%s";

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

	@Override
	public IdGenerator getObject() {
		int length = redisTemplate.keys(namespace + ":snowflake:app:*").size();
		if (length >= MAX_CLUSTER_NODES) {
			throw new IllegalStateException();
		}
		String key;
		long workerId, datacenterId;
		do {
			workerId = workerIdGen.getAndIncrement();
			if (workerId >= MAX_SLOT) {
				workerIdGen.set(0);
				datacenterIdGen.getAndIncrement();
			}
			datacenterId = datacenterIdGen.get();
			if (datacenterId >= MAX_SLOT) {
				datacenterIdGen.set(0);
			}
			key = String.format(KEY_PATTERN, namespace, datacenterId, workerId);
		} while (redisTemplate.hasKey(key));

		redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()), 60, TimeUnit.SECONDS);
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
		String key = String.format(KEY_PATTERN, namespace, datacenterId, workerId);
		if (key.equals(expiredKey)) {
			redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()), 60, TimeUnit.SECONDS);
			if (log.isInfoEnabled()) {
				log.info("Renew datacenterId: {}, workerId: {}", datacenterId, workerId);
			}
		}
	}

}
