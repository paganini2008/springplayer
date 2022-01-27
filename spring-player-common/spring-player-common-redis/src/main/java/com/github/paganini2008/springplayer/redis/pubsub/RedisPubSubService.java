package com.github.paganini2008.springplayer.redis.pubsub;

/**
 * 
 * RedisPubSubService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface RedisPubSubService {

	void convertAndUnicast(String channel, Object message);

	void convertAndMulticast(String channel, Object message);

}