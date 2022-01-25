package com.github.paganini2008.springplayer.webmvc.monitor;

import lombok.Data;

/**
 * 
 * Span
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Data
public class Span {

	private String traceId;
	private int span;
	private int parentSpan;
	private String path;
	private String timestamp;
	private long downstreamElapsed;
	private long upstreamElapsed;
	private int status;
	
}
