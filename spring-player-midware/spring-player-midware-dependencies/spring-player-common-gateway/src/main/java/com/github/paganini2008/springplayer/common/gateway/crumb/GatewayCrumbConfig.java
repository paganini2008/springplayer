package com.github.paganini2008.springplayer.common.gateway.crumb;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.github.paganini2008.springplayer.common.monitor.crumb.ThresholdTraceStore;
import com.github.paganini2008.springplayer.common.monitor.crumb.TraceStore;

/**
 * 
 * GatewayCrumbConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ComponentScan("com.github.paganini2008.springplayer.common.gateway.crumb")
@ConditionalOnWebApplication(type = Type.REACTIVE)
@Configuration(proxyBeanMethods = false)
public class GatewayCrumbConfig {

	@Bean
	public RedisAtomicLong spanGen(RedisConnectionFactory connectionFactory) {
		return new RedisAtomicLong("springplayer:web:crumb:spanGen", connectionFactory);
	}

	@Bean
	public TraceStore defaultTraceStore() {
		return new ThresholdTraceStore(null, 0L, null);
	}

}
