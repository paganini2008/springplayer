package com.github.paganini2008.springplayer.messenger.service;

import static com.github.paganini2008.springplayer.messenger.config.AmqpMessagingConfig.DEFAULT_EXCHANGE_NAME;
import static com.github.paganini2008.springplayer.messenger.config.AmqpMessagingConfig.DEFAULT_QUEUE_NAME;
import static com.github.paganini2008.springplayer.messenger.config.AmqpMessagingConfig.DEFAULT_ROUTING_KEY;

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

import com.github.paganini2008.springplayer.common.messenger.model.MessageDTO;
import com.github.paganini2008.springplayer.common.utils.JacksonUtils;
import com.rabbitmq.client.Channel;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * AmqpMessageService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@SuppressWarnings("all")
@Slf4j
@ConditionalOnProperty(name = "spring.player.messenger.store", havingValue = "amqp")
@ConditionalOnClass(RabbitTemplate.class)
@Service
public class AmqpMessageService implements MessageService {

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private MessageConfirmCallback confirmCallback;

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	private DingTalkSenderService dingTalkSenderService;

	@Autowired
	private PopupSenderService popupSenderService;

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
		MessageDTO entity = (MessageDTO) message;
		switch (entity.getType()) {
		case DING_TALK:
			dingTalkSenderService.processSendingDingTalk(entity.getDingTalk());
			break;
		case EMAIL:
			emailSenderService.processSendingEmail(entity.getEmail());
			break;
		case POPUP:
			popupSenderService.processSendingPopup(entity.getPopup());
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	@SneakyThrows
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = DEFAULT_QUEUE_NAME, durable = "true"), exchange = @Exchange(name = DEFAULT_EXCHANGE_NAME, durable = "true", type = "topic", ignoreDeclarationExceptions = "true"), key = DEFAULT_ROUTING_KEY))
	@RabbitHandler
	public void onData(Message message, Channel channel) {
		MessageDTO entity = JacksonUtils.parseJson(message.getBody(), MessageDTO.class);
		handleMessage(entity);
		log.info("Header: {}", message.getMessageProperties().getHeaders());
		Long deliveryTag = message.getMessageProperties().getDeliveryTag();
		channel.basicAck(deliveryTag, false);

	}

}
