package com.github.paganini2008.springplayer.monitor.crumb;

/**
 * 
 * TraceStore
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface TraceStore {

	boolean shouldTrace(Span span);

	void trace(SpanTree spanTree);

}
