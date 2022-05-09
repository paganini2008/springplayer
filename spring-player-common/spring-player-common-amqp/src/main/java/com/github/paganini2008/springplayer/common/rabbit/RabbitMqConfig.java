package com.github.paganini2008.springplayer.common.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * RabbitMqConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class RabbitMqConfig {

	@Autowired
	public void configureRabbitTemplate(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
	}

}
