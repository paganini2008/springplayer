package com.github.paganini2008.springplayer.crumb.webmvc;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.codec.Decoder;

/**
 * 
 * WebMvcCrumbConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ComponentScan("com.github.paganini2008.springplayer.crumb.webmvc")
@Configuration(proxyBeanMethods = false)
public class WebMvcCrumbConfig {

	@Bean
	public RedisAtomicLong spanGen(RedisConnectionFactory connectionFactory) {
		return new RedisAtomicLong("springplayer:web:crumb:spanGen", connectionFactory);
	}

	@Bean
	public Decoder remoteCallSpanPostDecoder(ObjectFactory<HttpMessageConverters> messageConverters, ObjectMapper objectMapper) {
		return new RpcSpanPostHandler(new SpringDecoder(messageConverters), objectMapper);
	}

}
