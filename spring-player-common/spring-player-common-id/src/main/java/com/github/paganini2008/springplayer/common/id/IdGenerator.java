package com.github.paganini2008.springplayer.common.id;

/**
 * 
 * IdGenerator
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface IdGenerator {

	long currentId();
	
	long generateId();
	
}
