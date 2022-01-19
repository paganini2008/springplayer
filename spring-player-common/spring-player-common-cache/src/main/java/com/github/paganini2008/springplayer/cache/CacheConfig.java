package com.github.paganini2008.springplayer.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * 
 * CacheConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration
public class CacheConfig {

	@Primary
	@Bean("caffeineCacheManager")
	public CacheManager caffeineCacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager();
		cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterAccess(1, TimeUnit.DAYS).initialCapacity(10).maximumSize(100));
		return cacheManager;
	}
}
