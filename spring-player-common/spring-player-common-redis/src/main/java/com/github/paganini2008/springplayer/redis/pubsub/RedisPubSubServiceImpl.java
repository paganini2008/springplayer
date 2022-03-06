package com.github.paganini2008.springplayer.redis.pubsub;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

/**
 * 
 * RedisPubSubServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class RedisPubSubServiceImpl implements RedisPubSubService {

	private final PubSubRedisTemplate redisTemplate;

	public void convertAndUnicast(String channel, Object message) {
		redisTemplate.convertAndUnicast(channel, message);
	}

	public void convertAndMulticast(String channel, Object message) {
		redisTemplate.convertAndMulticast(channel, message);
	}

	@Override
	public void convertAndUnicast(String channel, Object message, long delay, TimeUnit timeUnit) {
		// TODO Auto-generated method stub

	}

	@Override
	public void convertAndMulticast(String channel, Object message, long delay, TimeUnit timeUnit) {
		// TODO Auto-generated method stub

	}

}
