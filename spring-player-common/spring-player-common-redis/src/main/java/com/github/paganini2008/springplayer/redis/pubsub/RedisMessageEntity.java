package com.github.paganini2008.springplayer.redis.pubsub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * RedisMessageEntity
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class RedisMessageEntity {

	private String channel;
	private PubSubMode mode;
	private Object message;

	public RedisMessageEntity() {
	}

	public RedisMessageEntity(String channel, PubSubMode mode, Object message) {
		this.channel = channel;
		this.mode = mode;
		this.message = message;
	}

}
