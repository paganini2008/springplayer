package com.github.paganini2008.springplayer.redis.pubsub;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

/**
 * 
 * RedisPubSubServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class RedisPubSubServiceImpl implements RedisPubSubService {

	private final PubSubRedisTemplate redisTemplate;

	public RedisPubSubServiceImpl(String keyNamespace, String channel, RedisConnectionFactory connectionFactory) {
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		jackson2JsonRedisSerializer.setObjectMapper(om);

		PubSubRedisTemplate redisTemplate = new PubSubRedisTemplate(keyNamespace, channel);
		redisTemplate.setConnectionFactory(connectionFactory);
		StringRedisSerializer stringSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.afterPropertiesSet();

		this.redisTemplate = redisTemplate;
	}
	
	public RedisPubSubServiceImpl(PubSubRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void convertAndUnicast(String channel, Object message) {
		redisTemplate.convertAndUnicast(channel, message);
	}

	public void convertAndMulticast(String channel, Object message) {
		redisTemplate.convertAndMulticast(channel, message);
	}

	@Override
	public void convertAndUnicast(String channel, Object message, long delay, TimeUnit timeUnit) {
		redisTemplate.convertAndUnicast(channel, message, delay, timeUnit);
	}

	@Override
	public void convertAndMulticast(String channel, Object message, long delay, TimeUnit timeUnit) {
		redisTemplate.convertAndMulticast(channel, message, delay, timeUnit);
	}

}
