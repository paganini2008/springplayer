package com.github.paganini2008.springplayer.messenger.service;

import static com.github.paganini2008.springplayer.messenger.MessengerConstants.MESSAGING_STREAM_TOPIC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.github.paganini2008.springplayer.common.redis.pubsub.RedisPubSubService;

/**
 * 
 * RedisMessagingService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ConditionalOnProperty(name = "spring.player.messenger.store", havingValue = "redis", matchIfMissing = true)
@Service
public class RedisMessageService implements MessageService {

	@Autowired
	private RedisPubSubService redisPubSubService;

	@Override
	public void sendMessage(Object payload) {
		redisPubSubService.convertAndUnicast(MESSAGING_STREAM_TOPIC, payload);
	}

	@Override
	public void handleMessage(Object message) {
	}

}
