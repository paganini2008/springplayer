package com.github.paganini2008.springplayer.crumb;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.LongAdder;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;

/**
 * 
 * SimpleTpsUpdater
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class SimpleTpsUpdater implements TpsUpdater, Runnable, InitializingBean {

	private final LongAdder counter;
	private final TaskScheduler scheduler;

	public SimpleTpsUpdater(TaskScheduler scheduler) {
		this.counter = new LongAdder();
		this.scheduler = scheduler;
	}

	private volatile long latestCount;
	private volatile int tps;

	private ScheduledFuture<?> future;

	@Override
	public void increment() {
		counter.increment();
	}

	@Override
	public int get() {
		return tps;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (future == null) {
			future = scheduler.scheduleWithFixedDelay(this, Duration.ofSeconds(1));
		}
	}

	@Override
	public void run() {
		long currentCount = counter.longValue();
		if (currentCount > 0) {
			tps = (int) (currentCount - latestCount);
			latestCount = currentCount;
		}
	}

}
