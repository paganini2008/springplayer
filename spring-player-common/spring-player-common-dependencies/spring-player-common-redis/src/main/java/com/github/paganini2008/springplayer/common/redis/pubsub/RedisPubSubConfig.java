package com.github.paganini2008.springplayer.common.redis.pubsub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.redis.RedisConfig;

/**
 * 
 * RedisPubSubConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@AutoConfigureAfter({ RedisConfig.class })
@Configuration(proxyBeanMethods = false)
public class RedisPubSubConfig {

	@Value("${spring.application.redis.pubsub.channel:}")
	private String pubsubChannel;

	@Value("${spring.application.redis.pubsub.keyNamespace:pubsub}")
	private String keyNamespace;

	@Value("${spring.application.name}")
	private String applicationName;

	@Bean
	public RedisMessageEventPublisher redisMessageEventPublisher(RedisTemplate<String, Object> redisTemplate) {
		return new RedisMessageEventPublisher(keyNamespace, redisTemplate);
	}

	@Bean
	public RedisMessageEventDispatcher redisMessageEventDispatcher() {
		return new RedisMessageEventDispatcher();
	}

	@Bean
	public RedisPubSubService redisPubSubService(RedisConnectionFactory redisConnectionFactory) {
		return new RedisPubSubServiceImpl(keyNamespace, getChannel(), redisConnectionFactory);
	}

	@Bean
	public KeyExpirationEventMessageListener keyExpirationEventMessageListener(
			RedisMessageListenerContainer redisMessageListenerContainer) {
		KeyExpirationEventMessageListener listener = new KeyExpirationEventMessageListener(redisMessageListenerContainer);
		listener.setKeyspaceNotificationsConfigParameter("Ex");
		return listener;
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
		redisMessageListenerContainer.addMessageListener(messageListener, new ChannelTopic(getChannel()));
		return redisMessageListenerContainer;
	}

	String getChannel() {
		return StringUtils.isNotBlank(pubsubChannel) ? pubsubChannel : applicationName;
	}

}
