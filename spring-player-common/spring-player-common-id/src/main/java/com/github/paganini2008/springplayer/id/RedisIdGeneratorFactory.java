package com.github.paganini2008.springplayer.id;

import org.springframework.data.redis.connection.RedisConnectionFactory;

import lombok.RequiredArgsConstructor;

/**
 * 
 * RedisIdGeneratorFactory
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class RedisIdGeneratorFactory implements IdGeneratorFactory {

	private final RedisConnectionFactory redisConnectionFactory;

	@Override
	public IdGenerator getObject() throws Exception {
		return new RedisIdGenerator(redisConnectionFactory);
	}

	@Override
	public Class<?> getObjectType() {
		return RedisIdGenerator.class;
	}

}
