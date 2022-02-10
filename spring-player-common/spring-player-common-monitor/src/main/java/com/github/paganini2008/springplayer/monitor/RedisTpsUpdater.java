package com.github.paganini2008.springplayer.monitor;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

/**
 * 
 * RedisTpsUpdater
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class RedisTpsUpdater implements TpsUpdater, DisposableBean {

	private final RedisAtomicLong counter;

	public RedisTpsUpdater(String name, RedisConnectionFactory redisConnectionFactory) {
		this.counter = new RedisAtomicLong(name, redisConnectionFactory);
	}

	private volatile long latestCount;
	private volatile int tps;

	@Override
	public void increment() {
		counter.incrementAndGet();
	}

	@Override
	public int get() {
		return tps;
	}

	@Override
	public void set() {
		long currentCount = counter.get();
		if (currentCount > 0) {
			tps = (int) (currentCount - latestCount);
			latestCount = currentCount;
		}
	}

	@Override
	public void destroy() throws Exception {
		counter.expire(3, TimeUnit.SECONDS);
	}

}
