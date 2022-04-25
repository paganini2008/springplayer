package com.github.paganini2008.springplayer.gateway.monitor;

/**
 * 
 * UserMetricDefinition
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public interface UserMetricDefinition<M> {

	M define(HttpResponseTrace trace);
	
}
