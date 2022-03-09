package com.github.paganini2008.springplayer.redis.pubsub;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * 
 * PubSubRedisTemplate
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class PubSubRedisTemplate extends RedisTemplate<String, Object> {

	private final String keyNamespace;
	private final String pubsubChannel;

	public PubSubRedisTemplate(String keyNamespace, String pubsubChannel) {
		super();
		this.keyNamespace = keyNamespace;
		this.pubsubChannel = pubsubChannel;
	}

	private String keyFor(String channel) {
		return keyNamespace + ":" + channel;
	}

	public void convertAndUnicast(String channel, Object message) {
		String key = keyFor(channel);
		opsForList().leftPush(key, new RedisMessageEntity(channel, PubSubMode.UNICAST, message));
		super.convertAndSend(pubsubChannel, new RedisMessageEntity(channel, PubSubMode.UNICAST, null));
	}

	public void convertAndMulticast(String channel, Object message) {
		super.convertAndSend(pubsubChannel, new RedisMessageEntity(channel, PubSubMode.MULTICAST, message));
	}

	public void convertAndUnicast(String channel, Object message, long delay, TimeUnit timeUnit) {
		String key = keyFor(channel);
		opsForList().leftPush(key + "__", new RedisMessageEntity(channel, PubSubMode.UNICAST, message));
		opsForValue().set(key, new RedisMessageEntity(channel, PubSubMode.UNICAST, null), delay, timeUnit);
	}

	public void convertAndMulticast(String channel, Object message, long delay, TimeUnit timeUnit) {
		String key = keyFor(channel);
		opsForValue().set(key + "__", new RedisMessageEntity(channel, PubSubMode.MULTICAST, message));
		opsForValue().set(key, new RedisMessageEntity(channel, PubSubMode.MULTICAST, null), delay, timeUnit);
	}

}
