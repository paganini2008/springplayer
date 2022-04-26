package com.github.paganini2008.springplayer.common.id;

/**
 * 
 * IdGenerator
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public interface IdGenerator {

	long currentId();
	
	long generateId();
	
}
