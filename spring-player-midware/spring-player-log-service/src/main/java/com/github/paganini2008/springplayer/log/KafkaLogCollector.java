package com.github.paganini2008.springplayer.log;

import static com.github.paganini2008.springplayer.common.Constants.KAFKA_TOPIC_LOG_COLLECTOR;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.github.paganini2008.springplayer.log.utils.LogEntry;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * KafkaLogCollector
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class KafkaLogCollector {

	@Autowired
	private RestHighLevelClient esHttpClient;

	@KafkaListener(topics = { KAFKA_TOPIC_LOG_COLLECTOR })
	public void onMessage(ConsumerRecord<String, LogEntry> record) {
		LogEntry logEntry = record.value();
		index(logEntry);
	}

	private void index(LogEntry logEntry) {
		log.info(logEntry.toString());
	}

}
