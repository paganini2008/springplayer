package com.github.paganini2008.springplayer.messenger.service;

import static com.github.paganini2008.springplayer.messenger.MessengerConstants.MESSAGING_STREAM_TOPIC;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.github.paganini2008.springplayer.messenger.entity.MessagingEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * KafkaMessagingService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@ConditionalOnProperty(name = "spring.player.platform.messaging.store", havingValue = "kafka")
@ConditionalOnClass(KafkaTemplate.class)
@Service
public class KafkaMessagingService implements MessagingService {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	private DingTalkSenderService dingTalkSenderService;

	@Override
	public void sendMessage(Object message) {
		kafkaTemplate.send(MESSAGING_STREAM_TOPIC, message);
	}

	@Override
	public void handleMessage(Object message) {
		MessagingEntity entity = (MessagingEntity) message;
		switch (entity.getType()) {
		case DING_TALK:
			dingTalkSenderService.processSendingDingTalk(entity.getDingTalk());
			break;
		case EMAIL:
			emailSenderService.processSendingEmail(entity.getEmail());
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	@KafkaListener(topics = { MESSAGING_STREAM_TOPIC })
	public void onMessage(ConsumerRecord<String, MessagingEntity> record) {
		log.info("Topic: {}, Message: {}", record.topic(), record.value());
		handleMessage(record.value());
	}

}
