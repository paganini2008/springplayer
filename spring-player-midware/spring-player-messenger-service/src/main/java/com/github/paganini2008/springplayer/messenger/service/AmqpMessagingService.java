package com.github.paganini2008.springplayer.messenger.service;

import static com.github.paganini2008.springplayer.messenger.config.RabbitMessagingConfig.DEFAULT_EXCHANGE_NAME;
import static com.github.paganini2008.springplayer.messenger.config.RabbitMessagingConfig.DEFAULT_QUEUE_NAME;
import static com.github.paganini2008.springplayer.messenger.config.RabbitMessagingConfig.DEFAULT_ROUTING_KEY;

import java.util.UUID;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.github.paganini2008.springplayer.common.JacksonUtils;
import com.github.paganini2008.springplayer.messenger.entity.MessagingEntity;
import com.rabbitmq.client.Channel;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * AmqpMessagingService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@SuppressWarnings("all")
@Slf4j
@ConditionalOnProperty(name = "spring.player.platform.messaging.store", havingValue = "amqp")
@ConditionalOnClass(RabbitTemplate.class)
@Service
public class AmqpMessagingService implements MessagingService {

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private MessageConfirmCallback confirmCallback;

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	private DingTalkSenderService dingTalkSenderService;

	@Override
	public void sendMessage(Object data) {
		byte[] bytes = JacksonUtils.toJsonStringBytes(data);
		Message message = new Message(bytes);
		message.setDefaultEncoding("UTF-8");
		CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
		MessagePostProcessor mpp = new MessagePostProcessor() {

			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				log.info("Post message: " + message);
				return message;
			}
		};
		template.convertAndSend(DEFAULT_EXCHANGE_NAME, DEFAULT_ROUTING_KEY, message, mpp, correlationData);
	}

	@Override
	public void handleMessage(Object message) {
		MessagingEntity entity = (MessagingEntity) message;
		switch (entity.getType()) {
		case DING_TALK:
			dingTalkSenderService.processSendingDingTalk(entity.getDingTalk());
			break;
		case EMAIL:
			emailSenderService.processSendingEmail(entity.getEmail());
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	@SneakyThrows
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = DEFAULT_QUEUE_NAME, durable = "true"), exchange = @Exchange(name = DEFAULT_EXCHANGE_NAME, durable = "true", type = "topic", ignoreDeclarationExceptions = "true"), key = DEFAULT_ROUTING_KEY))
	@RabbitHandler
	public void onData(Message message, Channel channel) {
		MessagingEntity entity = JacksonUtils.parseJson(message.getBody(), MessagingEntity.class);
		handleMessage(entity);
		log.info("Header: {}", message.getMessageProperties().getHeaders());
		Long deliveryTag = message.getMessageProperties().getDeliveryTag();
		channel.basicAck(deliveryTag, false);

	}

}
