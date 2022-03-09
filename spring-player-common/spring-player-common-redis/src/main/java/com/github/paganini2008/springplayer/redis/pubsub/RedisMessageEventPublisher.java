package com.github.paganini2008.springplayer.redis.pubsub;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.core.RedisOperations;

import com.github.paganini2008.devtools.CharsetUtils;

/**
 * 
 * RedisMessageEventPublisher
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class RedisMessageEventPublisher implements ApplicationEventPublisherAware {

	private final String keyNamespace;
	private final RedisOperations<String, Object> redisTemplate;

	public RedisMessageEventPublisher(String keyNamespace, RedisOperations<String, Object> redisTemplate) {
		this.keyNamespace = keyNamespace;
		this.redisTemplate = redisTemplate;
	}

	private ApplicationEventPublisher applicationEventPublisher;

	public void doPubSub(RedisMessageEntity data) {
		String key = keyNamespace + ":" + data.getChannel();
		DataType dataType = redisTemplate.type(key);
		if (dataType == null) {
			return;
		}
		RedisMessageEntity messageEntity = null;
		if (dataType == DataType.LIST) {
			messageEntity = (RedisMessageEntity) redisTemplate.opsForList().leftPop(key);
		} else if (dataType == DataType.NONE) {
			messageEntity = data;
		}
		if (messageEntity != null) {
			applicationEventPublisher.publishEvent(new RedisMessageEvent(this, messageEntity.getChannel(), messageEntity.getMessage()));
		}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@EventListener(RedisKeyExpiredEvent.class)
	public void handleRedisKeyExpiredEvent(RedisKeyExpiredEvent<?> event) {
		String expiredKey = new String(event.getSource(), CharsetUtils.UTF_8);
		if (!expiredKey.startsWith(keyNamespace)) {
			return;
		}
		String key = expiredKey + "__";
		DataType dataType = redisTemplate.type(key);
		if (dataType == null) {
			return;
		}
		RedisMessageEntity messageEntity = null;
		if (dataType == DataType.LIST) {
			messageEntity = (RedisMessageEntity) redisTemplate.opsForList().leftPop(key);
		} else if (dataType == DataType.STRING) {
			messageEntity = (RedisMessageEntity) redisTemplate.opsForValue().get(key);
			redisTemplate.delete(key);
		}
		
		if (messageEntity != null) {
			applicationEventPublisher.publishEvent(new RedisMessageEvent(this, messageEntity.getChannel(), messageEntity.getMessage()));
		}
	}
}
