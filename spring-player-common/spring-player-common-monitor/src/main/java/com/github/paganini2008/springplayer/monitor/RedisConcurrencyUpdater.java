package com.github.paganini2008.springplayer.monitor;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

/**
 * 
 * RedisConcurrencyUpdater
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class RedisConcurrencyUpdater implements ConcurrencyUpdater {

	private final RedisAtomicLong counter;

	public RedisConcurrencyUpdater(String name, RedisConnectionFactory redisConnectionFactory) {
		this.counter = new RedisAtomicLong(name, redisConnectionFactory);
	}

	@Override
	public void increment() {
		counter.incrementAndGet();
	}

	@Override
	public int get() {
		return counter.intValue();
	}

	@Override
	public void decrement() {
		if (get() > 0) {
			counter.decrementAndGet();
		}
	}

	@Override
	public String toString() {
		return String.valueOf(get());
	}

}
