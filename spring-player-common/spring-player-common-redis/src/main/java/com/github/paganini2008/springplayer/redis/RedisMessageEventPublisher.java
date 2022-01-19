package com.github.paganini2008.springplayer.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 
 * RedisMessageEventPublisher
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class RedisMessageEventPublisher implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher;

	@Qualifier("pubsubRedisTemplate")
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

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
