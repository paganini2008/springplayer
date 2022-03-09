package com.github.paganini2008.springplayer.example.service;

import org.springframework.stereotype.Component;

import com.github.paganini2008.springplayer.redis.pubsub.RedisPubSub;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * ExampleService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Component
public class ExampleService {

	@RedisPubSub("testPubSub")
	public void onMessage(String channel, Object data) {
		log.info("Channel: {}, data: {}", channel, data);
	}

	@RedisPubSub("testDelayPubSub")
	public void onDelayMessage(String channel, Object data) {
		log.info("Channel: {}, data: {}", channel, data);
	}

}
