package com.github.paganini2008.springplayer.log.utils;

import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * 
 * LogEntryJsonSerializer
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class LogEntryJsonSerializer extends JsonSerializer<LogEntry> {

	public LogEntryJsonSerializer() {
		super();
	}
	
}
