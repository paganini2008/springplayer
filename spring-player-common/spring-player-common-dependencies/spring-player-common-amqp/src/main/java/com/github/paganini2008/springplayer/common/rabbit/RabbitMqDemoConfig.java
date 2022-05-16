package com.github.paganini2008.springplayer.common.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * RabbitMqDemoConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class RabbitMqDemoConfig {

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("${springplayer.common.amqp.topicRoutingKey:demo1}")
	private String topicRoutingKey;

	@Value("${springplayer.common.amqp.fanoutRoutingKey:demo2}")
	private String fanoutRoutingKey;

	@Value("${springplayer.common.amqp.directRoutingKey:demo3}")
	private String directRoutingKey;

	@Bean("topicExchange")
	public Exchange topicExchange() {
		String exchangeName = applicationName + ".topicExchange";
		return ExchangeBuilder.topicExchange(exchangeName).durable(true).autoDelete().build();
	}

	@Bean("fanoutExchange")
	public Exchange fanoutExchange() {
		String exchangeName = applicationName + ".fanoutExchange";
		return ExchangeBuilder.fanoutExchange(exchangeName).durable(true).autoDelete().build();
	}

	@Bean("directExchange")
	public Exchange directExchange() {
		String exchangeName = applicationName + ".directExchange";
		return ExchangeBuilder.directExchange(exchangeName).durable(true).autoDelete().build();
	}

	@Bean("topicQueue")
	public Queue topicQueue() {
		String queueName = applicationName + ".topicQueue";
		return QueueBuilder.durable(queueName).build();
	}

	@Bean("fanoutQueue")
	public Queue fanoutQueue() {
		String queueName = applicationName + ".fanoutQueue";
		return QueueBuilder.durable(queueName).build();
	}

	public Queue directQueue() {
		String queueName = applicationName + ".directQueue";
		return QueueBuilder.durable(queueName).build();
	}

	@Bean("topicQueueExchange")
	public Binding topicQueueExchange(@Qualifier("topicQueue") Queue queue, @Qualifier("topicExchange") Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(topicRoutingKey).noargs();
	}

	@Bean("fanoutQueueExchange")
	public Binding fanoutQueueExchange(@Qualifier("fanoutQueue") Queue queue, @Qualifier("fanoutExchange") Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(fanoutRoutingKey).noargs();
	}

	@Bean("directQueueExchange")
	public Binding directQueueExchange(@Qualifier("directQueue") Queue queue, @Qualifier("directExchange") Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(directRoutingKey).noargs();
	}

}
