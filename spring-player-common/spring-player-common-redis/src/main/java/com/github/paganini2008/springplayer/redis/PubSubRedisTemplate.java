package com.github.paganini2008.springplayer.redis;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * 
 * PubSubRedisTemplate
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class PubSubRedisTemplate extends RedisTemplate<String, Object> {

	private String pubsubChannel;

	public void setPubsubChannel(String pubsubChannel) {
		this.pubsubChannel = pubsubChannel;
	}

	public void pushMessage(String channel, Object message) {
		opsForList().leftPush(channel, message);
		RedisMessageEntity messageEntity = new RedisMessageEntity(channel, null);
		super.convertAndSend(pubsubChannel, messageEntity);
	}

	public void shareMessage(String channel, Object message) {
		RedisMessageEntity messageEntity = new RedisMessageEntity(channel, message);
		super.convertAndSend(pubsubChannel, messageEntity);
	}

}
