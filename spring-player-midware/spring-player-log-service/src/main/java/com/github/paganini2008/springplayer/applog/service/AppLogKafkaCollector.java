package com.github.paganini2008.springplayer.applog.service;

import static com.github.paganini2008.springplayer.common.Constants.KAFKA_TOPIC_APPLOG;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.github.paganini2008.springplayer.logging.common.AppLogEntry;

/**
 * 
 * AppLogKafkaCollector
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public class AppLogKafkaCollector {
	
	@Autowired
	private AppLogService appLogService;

	@KafkaListener(topics = { KAFKA_TOPIC_APPLOG })
	public void onMessage(ConsumerRecord<String, AppLogEntry> record) {
		AppLogEntry logEntry = record.value();
		index(logEntry);
	}

	private void index(AppLogEntry logEntry) {
		appLogService.saveAppLog(logEntry);
	}

}
