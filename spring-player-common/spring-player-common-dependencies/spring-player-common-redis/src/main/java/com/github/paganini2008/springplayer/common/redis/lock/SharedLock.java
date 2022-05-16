package com.github.paganini2008.springplayer.common.redis.lock;

import com.github.paganini2008.devtools.multithreads.latch.Latch;

/**
 * 
 * SharedLock
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface SharedLock extends Latch {
	
	String getLockName();

	int getExpiration();
}
