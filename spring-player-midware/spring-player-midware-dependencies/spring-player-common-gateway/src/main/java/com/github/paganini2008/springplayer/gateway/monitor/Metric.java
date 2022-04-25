package com.github.paganini2008.springplayer.gateway.monitor;

import java.util.Map;

/**
 * 
 * Metric
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public interface Metric {

	long getTimestamp();

	Map<String, Object> toEntries();

	Number getValue();

	Number getGrowthRate();
}
