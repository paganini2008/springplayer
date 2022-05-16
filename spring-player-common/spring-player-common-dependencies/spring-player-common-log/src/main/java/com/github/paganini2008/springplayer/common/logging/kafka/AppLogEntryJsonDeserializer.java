package com.github.paganini2008.springplayer.common.logging.kafka;

import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.github.paganini2008.springplayer.common.logging.AppLogEntry;

/**
 * 
 * AppLogEntryJsonDeserializer
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class AppLogEntryJsonDeserializer extends JsonDeserializer<AppLogEntry> {

	public AppLogEntryJsonDeserializer() {
		super();
	}

}
