package com.github.paganini2008.springplayer.common.logging.kafka;

import org.springframework.kafka.support.serializer.JsonSerializer;

import com.github.paganini2008.springplayer.common.logging.AppLogEntry;

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
