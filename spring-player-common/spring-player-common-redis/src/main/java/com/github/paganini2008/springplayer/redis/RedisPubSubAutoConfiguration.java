package com.github.paganini2008.springplayer.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

/**
 * 
 * RedisPubSubAutoConfiguration
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ConditionalOnBean(RedisConnectionFactory.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class RedisPubSubAutoConfiguration {

	@Value("${springplayer.redis.pubsub.channel:pubsub}")
	private String pubsubChannel;

	@Bean
	public RedisMessageEventPublisher redisMessageEventPublisher() {
		return new RedisMessageEventPublisher();
	}

	@Bean
	public RedisMessageEventHandler redisMessageEventHandler() {
		return new RedisMessageEventHandler();
	}

	@Bean
	public RedisPubSubService redisPubSubService(PubSubRedisTemplate pubSubRedisTemplate) {
		return new RedisPubSubService(pubSubRedisTemplate);
	}

	@Bean
	public MessageListenerAdapter redisMessageEventListener(RedisMessageEventPublisher eventPublisher) {
		MessageListenerAdapter adapter = new MessageListenerAdapter(eventPublisher, "doPubSub");
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		jackson2JsonRedisSerializer.setObjectMapper(om);

		adapter.setSerializer(jackson2JsonRedisSerializer);
		adapter.afterPropertiesSet();
		return adapter;
	}

	@Bean
	public RedisMessageListenerContainer redisMessageEventListenerContainer(RedisConnectionFactory redisConnectionFactory,
			MessageListenerAdapter messageListener) {
		RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
		redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
		redisMessageListenerContainer.addMessageListener(messageListener, new ChannelTopic(pubsubChannel));
		return redisMessageListenerContainer;
	}

	@Bean("pubsubRedisTemplate")
	public PubSubRedisTemplate pubsubRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		jackson2JsonRedisSerializer.setObjectMapper(om);

		PubSubRedisTemplate redisTemplate = new PubSubRedisTemplate();
		redisTemplate.setPubsubChannel(pubsubChannel);
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		StringRedisSerializer stringSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

}
