package com.github.paganini2008.springplayer.messenger.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * RabbitMessagingConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ConditionalOnProperty(name = "yl.platform.messaging.store", havingValue = "amqp")
@ConditionalOnClass(RabbitTemplate.class)
@Configuration(proxyBeanMethods = false)
public class RabbitMessagingConfig {

	public static final String DEFAULT_EXCHANGE_NAME = "messaging-center-exchange";
	public static final String DEFAULT_QUEUE_NAME = "messaging-center-queue";
	public static final String DEFAULT_ROUTING_KEY = "messaging-center-routing-key";

	@Bean(DEFAULT_EXCHANGE_NAME)
	public Exchange topicExchange() {
		return ExchangeBuilder.topicExchange(DEFAULT_EXCHANGE_NAME).durable(true).build();
	}

	@Bean(DEFAULT_QUEUE_NAME)
	public Queue topicQueue() {
		return QueueBuilder.durable(DEFAULT_QUEUE_NAME).build();
	}

	public Binding topicQueueExchange(@Qualifier(DEFAULT_QUEUE_NAME) Queue topicQueue,
			@Qualifier(DEFAULT_EXCHANGE_NAME) Exchange topicExchange) {
		return BindingBuilder.bind(topicQueue).to(topicExchange).with(DEFAULT_ROUTING_KEY).noargs();
	}

}
