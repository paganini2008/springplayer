package com.github.paganini2008.springplayer.common.monitor;

/**
 * 
 * TpsUpdater
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface TpsUpdater {

	void increment();

	int get();

	void set();

}
