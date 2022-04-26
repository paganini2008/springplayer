package com.github.paganini2008.springplayer.common.gateway.monitor;

/**
 * 
 * ConcurrencyUpdater
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface ConcurrencyUpdater {

	void increment();
	
	int get();
	
	void decrement();
	
}
