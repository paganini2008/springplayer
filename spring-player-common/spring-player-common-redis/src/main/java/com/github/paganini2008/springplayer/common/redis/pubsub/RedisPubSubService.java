package com.github.paganini2008.springplayer.common.redis.pubsub;

import java.util.concurrent.TimeUnit;

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

	void convertAndUnicast(String channel, Object message, long delay, TimeUnit timeUnit);

	void convertAndMulticast(String channel, Object message, long delay, TimeUnit timeUnit);

}