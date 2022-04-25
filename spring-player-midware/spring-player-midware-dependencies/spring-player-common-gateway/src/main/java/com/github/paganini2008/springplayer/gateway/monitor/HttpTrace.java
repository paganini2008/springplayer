package com.github.paganini2008.springplayer.gateway.monitor;

import java.util.Map;

/**
 * 
 * HttpTrace
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface HttpTrace {

	static final int MAX_REQUEST_TIMEOUT = 1 * 1000;
	static final int MAX_CONCURRENCY = 10;

	String getMethod();

	String getPath();

	Map<String, ?> getHeader();

	Map<String, ?> getCookie();

	long getTimestamp();

}
