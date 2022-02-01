package com.github.paganini2008.springplayer.crumb;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * Span
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Data
public class Span implements Serializable{

	private static final long serialVersionUID = -8144860740080719764L;
	private String traceId;
	private int span;
	private int parentSpan;
	private String path;
	private String timestamp;
	private long[] elapsed;
	private int status;
	private int concurrents;
	
	
}
