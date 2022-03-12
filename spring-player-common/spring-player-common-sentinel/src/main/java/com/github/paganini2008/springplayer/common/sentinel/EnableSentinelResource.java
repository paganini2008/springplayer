package com.github.paganini2008.springplayer.common.sentinel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.github.paganini2008.springplayer.common.sentinel.apollo.SentinelRuleApolloConfig;
import com.github.paganini2008.springplayer.common.sentinel.redis.SentinelRuleRedisConfig;

/**
 * 
 * EnableSentinelResource
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ SentinelRuleApolloConfig.class, SentinelRuleRedisConfig.class })
public @interface EnableSentinelResource {
}
