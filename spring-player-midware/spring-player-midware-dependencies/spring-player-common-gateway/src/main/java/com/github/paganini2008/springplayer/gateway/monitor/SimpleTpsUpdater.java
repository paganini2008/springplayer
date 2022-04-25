package com.github.paganini2008.springplayer.gateway.monitor;

import java.util.concurrent.atomic.LongAdder;

/**
 * 
 * SimpleTpsUpdater
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class SimpleTpsUpdater implements TpsUpdater {

	private final LongAdder counter;

	public SimpleTpsUpdater() {
		this.counter = new LongAdder();
	}

	private volatile long latestCount;
	private volatile int tps;

	@Override
	public void increment() {
		counter.increment();
	}

	@Override
	public int get() {
		return tps;
	}

	@Override
	public void set() {
		long currentCount = counter.longValue();
		if (currentCount > 0) {
			tps = (int) (currentCount - latestCount);
			latestCount = currentCount;
		}
	}

}
