package com.github.paganini2008.springplayer.redis.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * 
 * RedisMessageEventPublisher
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class RedisMessageEventPublisher implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private PubSubRedisTemplate redisTemplate;

	public void doPubSub(RedisMessageEntity entity) {
		Object message = entity.getMessage() != null ? entity.getMessage() : redisTemplate.opsForList().leftPop(entity.getChannel());
		if (message != null) {
			applicationEventPublisher.publishEvent(new RedisMessageEvent(this, entity.getChannel(), message));
		}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
}
