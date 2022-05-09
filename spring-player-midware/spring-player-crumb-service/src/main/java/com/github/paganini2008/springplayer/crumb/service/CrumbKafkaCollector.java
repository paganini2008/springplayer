package com.github.paganini2008.springplayer.crumb.service;

import static com.github.paganini2008.springplayer.crumb.CrumbConstants.*;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.github.paganini2008.springplayer.common.logging.AppLogEntry;

/**
 * 
 * CrumbKafkaCollector
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public class CrumbKafkaCollector {
	
	@Autowired
	private TraceSpanService traceSpanService;

	@KafkaListener(topics = { KAFKA_TOPIC_TRACE_SPAN })
	public void onMessage(ConsumerRecord<String, AppLogEntry> record) {
		AppLogEntry logEntry = record.value();
		index(logEntry);
	}

	private void index(AppLogEntry logEntry) {
		traceSpanService.saveTraceSpan(logEntry);
	}

}
