package com.github.paganini2008.springplayer.redis;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * 
 * RedisPubSubService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class RedisPubSubService {

	private final PubSubRedisTemplate redisTemplate;

	public void pushMessage(String channel, Object message) {
		redisTemplate.pushMessage(channel, message);
	}

	public void shareMessage(String channel, Object message) {
		redisTemplate.shareMessage(channel, message);
	}

}
