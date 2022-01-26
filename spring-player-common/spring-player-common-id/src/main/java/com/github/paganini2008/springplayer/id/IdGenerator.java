package com.github.paganini2008.springplayer.id;

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
