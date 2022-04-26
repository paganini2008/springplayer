package com.github.paganini2008.springplayer.common.gateway.monitor;

import java.util.Map;

/**
 * 
 * Metric
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface Metric {

	long getTimestamp();

	Map<String, Object> toEntries();

	Number getValue();

	Number getGrowthRate();
}
