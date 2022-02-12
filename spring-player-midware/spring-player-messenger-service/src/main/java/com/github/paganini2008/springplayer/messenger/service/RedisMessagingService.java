package com.github.paganini2008.springplayer.messenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.github.paganini2008.springplayer.messenger.entity.MessagingEntity;
import com.github.paganini2008.springplayer.redis.pubsub.RedisPubSub;
import com.github.paganini2008.springplayer.redis.pubsub.RedisPubSubService;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * RedisMessagingService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@ConditionalOnProperty(name = "yl.platform.messaging.store", havingValue = "redis", matchIfMissing = true)
@Service
public class RedisMessagingService implements MessagingService {

	@Autowired
	private RedisPubSubService redisPubSubService;

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	private DingTalkSenderService dingTalkSenderService;

	@Override
	public void sendMessage(Object message) {
		redisPubSubService.convertAndUnicast("abc", message);
	}

	@Override
	public void handleMessage(Object message) {
		onMessage("abc", message);

	}

	@Async
	@SneakyThrows
	@RedisPubSub("abc")
	public void onMessage(String channel, Object message) {
		log.info("Channel: {}, Message: {}", channel, message);
		MessagingEntity messagingEntity = (MessagingEntity) message;
		switch (messagingEntity.getType()) {
		case DING_TALK:
			dingTalkSenderService.processSendingDingTalk(messagingEntity.getDingTalk());
			break;
		case EMAIL:
			emailSenderService.processSendingEmail(messagingEntity.getEmail());
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

}
