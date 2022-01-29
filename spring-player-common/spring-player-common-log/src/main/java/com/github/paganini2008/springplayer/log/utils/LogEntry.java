package com.github.paganini2008.springplayer.log.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * LogEntry
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class LogEntry {
	
	private String clusterName;
	private String applicationName;
	private String host;
	private String traceId;
	private int span;
	private String identifier;
	private String args;
	private String loggerName;
	private String message;
	private String level;
	private String error;
	private String marker;
	private long timestamp;

}
