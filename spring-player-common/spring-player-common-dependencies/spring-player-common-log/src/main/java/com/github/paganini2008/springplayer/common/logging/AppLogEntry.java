package com.github.paganini2008.springplayer.common.logging;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * AppLogEntry
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class AppLogEntry {
	
	private String clusterName;
	private String applicationName;
	private String host;
	private String traceId;
	private Integer span;
	private String identifier;
	private String args;
	private String loggerName;
	private String message;
	private String level;
	private String error;
	private String marker;
	private Date createTime;

}
