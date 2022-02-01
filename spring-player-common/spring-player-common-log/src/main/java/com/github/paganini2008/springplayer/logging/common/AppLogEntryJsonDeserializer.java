package com.github.paganini2008.springplayer.logging.common;

import org.springframework.kafka.support.serializer.JsonDeserializer;

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
