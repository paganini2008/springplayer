package com.github.paganini2008.springplayer.gateway.monitor;

import com.github.paganini2008.devtools.time.MergeableFunction;

/**
 * 
 * UserMetric
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface UserMetric<T> extends Metric, MergeableFunction<T>, Comparable<T> {

	T empty();

}
