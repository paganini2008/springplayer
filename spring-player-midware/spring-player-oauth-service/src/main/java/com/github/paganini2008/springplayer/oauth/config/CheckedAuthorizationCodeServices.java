package com.github.paganini2008.springplayer.oauth.config;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Component;

/**
 * 
 * CheckedAuthorizationCodeServices
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public class CheckedAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

	private final RedisTemplate<String, Object> redisTemplate;

	public CheckedAuthorizationCodeServices(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.afterPropertiesSet();
		this.redisTemplate = redisTemplate;
	}

	@Override
	protected void store(String code, OAuth2Authentication authentication) {
		redisTemplate.opsForValue().set(code, authentication, 5, TimeUnit.MINUTES);
	}

	@Override
	protected OAuth2Authentication remove(String code) {
		OAuth2Authentication auth2Authentication = (OAuth2Authentication) redisTemplate.opsForValue().get(code);
		if (auth2Authentication != null) {
			redisTemplate.delete(code);
		}
		return auth2Authentication;
	}

}
