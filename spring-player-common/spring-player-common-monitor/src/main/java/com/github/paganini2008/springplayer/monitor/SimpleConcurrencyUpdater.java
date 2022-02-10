package com.github.paganini2008.springplayer.monitor;

import java.util.concurrent.atomic.LongAdder;

/**
 * 
 * SimpleConcurrencyUpdater
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class SimpleConcurrencyUpdater implements ConcurrencyUpdater {

	private final LongAdder counter;

	public SimpleConcurrencyUpdater() {
		this.counter = new LongAdder();
	}

	@Override
	public void increment() {
		counter.increment();
	}

	@Override
	public int get() {
		return counter.intValue();
	}

	@Override
	public void decrement() {
		if (get() > 0) {
			counter.decrement();
		}
	}

	@Override
	public String toString() {
		return String.valueOf(get());
	}

}
