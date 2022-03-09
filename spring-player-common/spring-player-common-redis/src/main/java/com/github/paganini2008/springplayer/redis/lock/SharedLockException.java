package com.github.paganini2008.springplayer.redis.lock;

/**
 * 
 * SharedLockException
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class SharedLockException extends IllegalStateException {

	private static final long serialVersionUID = 1025578364577093323L;
	
	public SharedLockException(String msg) {
		super(msg);
	}

	public SharedLockException(Throwable e) {
		super(e);
	}

	public SharedLockException(String msg, Throwable e) {
		super(msg, e);
	}

}
