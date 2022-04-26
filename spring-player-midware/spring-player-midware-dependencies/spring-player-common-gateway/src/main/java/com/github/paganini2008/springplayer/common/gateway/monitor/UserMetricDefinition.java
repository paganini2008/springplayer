package com.github.paganini2008.springplayer.common.gateway.monitor;

/**
 * 
 * UserMetricDefinition
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface UserMetricDefinition<M> {

	M define(HttpResponseTrace trace);
	
}
