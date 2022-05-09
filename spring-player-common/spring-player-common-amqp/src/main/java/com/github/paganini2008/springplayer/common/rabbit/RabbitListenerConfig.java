package com.github.paganini2008.springplayer.common.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 * 
 * RabbitListenerConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class RabbitListenerConfig implements RabbitListenerConfigurer {

	@Autowired
	public ConnectionFactory connectionFactory;

	@Bean
	public DefaultMessageHandlerMethodFactory handlerMethodFactory() {
		DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
		factory.setMessageConverter(new MappingJackson2MessageConverter());
		return factory;
	}

	@Override
	public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
		rabbitListenerEndpointRegistrar.setMessageHandlerMethodFactory(handlerMethodFactory());
	}

}
