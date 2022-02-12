package com.github.paganini2008.springplayer.messenger.service;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * MessageConfirmCallback
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Component
public class MessageConfirmCallback implements ConfirmCallback {

	@Autowired
	public void configure(RabbitTemplate template) {
		template.setConfirmCallback(this);
	}

	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		log.info("消息ACK结果:" + ack + ", correlationData: " + correlationData.getId());
	}

}
