package com.github.paganini2008.springplayer.redis;

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
	private Object message;

	public RedisMessageEntity() {
	}

	public RedisMessageEntity(String channel,  Object message) {
		this.channel = channel;
		this.message = message;
	}

}
