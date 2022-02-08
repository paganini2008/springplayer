package com.github.paganini2008.springplayer.crumb;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.scheduling.TaskScheduler;

/**
 * 
 * RedisTpsUpdater
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class RedisTpsUpdater implements TpsUpdater, Runnable, InitializingBean, DisposableBean {

	private final RedisAtomicLong counter;
	private final TaskScheduler scheduler;

	public RedisTpsUpdater(String name, RedisConnectionFactory redisConnectionFactory, TaskScheduler scheduler) {
		this.counter = new RedisAtomicLong(name, redisConnectionFactory);
		this.scheduler = scheduler;
	}

	private volatile long latestCount;
	private volatile int tps;
	private ScheduledFuture<?> future;

	@Override
	public void increment() {
		counter.incrementAndGet();
	}

	@Override
	public int get() {
		return tps;
	}

	@Override
	public void run() {
		long currentCount = counter.get();
		if (currentCount > 0) {
			tps = (int) (currentCount - latestCount);
			latestCount = currentCount;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (future == null) {
			future = scheduler.scheduleWithFixedDelay(this, Duration.ofSeconds(1));
		}
	}

	@Override
	public void destroy() throws Exception {
		counter.expire(3, TimeUnit.SECONDS);
	}

}
