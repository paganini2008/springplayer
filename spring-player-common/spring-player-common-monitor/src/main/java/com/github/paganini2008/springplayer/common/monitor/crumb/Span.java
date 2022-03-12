package com.github.paganini2008.springplayer.common.monitor.crumb;

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
public class Span implements Serializable {

	private static final long serialVersionUID = -8144860740080719764L;
	private String traceId;
	private long spanId;
	private String path;
	private long timestamp;
	private long elapsed;
	private int status;
	private int concurrents;
	private long parentSpanId;

	public Span() {
	}

	public Span(String traceId, long spanId) {
		this.traceId = traceId;
		this.spanId = spanId;
	}

}
