package com.github.paganini2008.springplayer.logging.common;

import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * 
 * AppLogEntryJsonSerializer
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class AppLogEntryJsonSerializer extends JsonSerializer<AppLogEntry> {

	public AppLogEntryJsonSerializer() {
		super();
	}
	
}
