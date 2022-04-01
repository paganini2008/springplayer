package com.github.paganini2008.springplayer.messenger.service;

import static com.github.paganini2008.springplayer.messenger.MessengerConstants.MESSAGING_STREAM_TOPIC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.github.paganini2008.springplayer.common.messenger.model.MessageDTO;
import com.github.paganini2008.springplayer.common.redis.pubsub.RedisPubSub;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * RedisMessageHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@ConditionalOnProperty(name = "spring.player.messenger.store", havingValue = "redis", matchIfMissing = true)
@Component
public class RedisMessageHandler {

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	private DingTalkSenderService dingTalkSenderService;

	@Autowired
	private PopupSenderService popupSenderService;
	
	@Async
	@SneakyThrows
	@RedisPubSub(MESSAGING_STREAM_TOPIC)
	public void onMessage(String channel, Object data) {
		log.info("Channel: {}, Message: {}", channel, data);
		MessageDTO message = (MessageDTO) data;
		if (message.getType() != null) {
			switch (message.getType()) {
			case DING_TALK:
				dingTalkSenderService.processSendingDingTalk(message.getDingTalk());
				break;
			case EMAIL:
				emailSenderService.processSendingEmail(message.getEmail());
				break;
			case POPUP:
				popupSenderService.processSendingPopup(message.getPopup());
				break;
			default:
				throw new UnsupportedOperationException();
			}
		}
	}
	
}
