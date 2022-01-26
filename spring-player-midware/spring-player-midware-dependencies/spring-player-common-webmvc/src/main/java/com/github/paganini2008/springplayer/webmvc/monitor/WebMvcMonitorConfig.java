package com.github.paganini2008.springplayer.webmvc.monitor;

import javax.servlet.Filter;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.RequestInterceptor;
import feign.codec.Decoder;

/**
 * 
 * WebMvcMonitorConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class WebMvcMonitorConfig {

	@Bean
	public RedisAtomicLong spanGen(RedisConnectionFactory connectionFactory) {
		return new RedisAtomicLong("springplayer:common:webmvc:monitor:spanGen", connectionFactory);
	}

	@Bean
	public Filter callSpanPreIterceptor(RedisAtomicLong spanGen) {
		return new CallSpanPreIterceptor(spanGen);
	}

	@Bean
	public RequestInterceptor remoteCallSpanInterceptor(RedisAtomicLong spanGen) {
		return new RemoteCallSpanPreInterceptor(spanGen);
	}

	@Bean
	public Decoder remoteCallSpanPostDecoder(ObjectFactory<HttpMessageConverters> messageConverters, ObjectMapper objectMapper) {
		return new RemoteCallSpanPostDecoder(new SpringDecoder(messageConverters), objectMapper);
	}

}
