package com.github.paganini2008.springplayer.crumb;

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
