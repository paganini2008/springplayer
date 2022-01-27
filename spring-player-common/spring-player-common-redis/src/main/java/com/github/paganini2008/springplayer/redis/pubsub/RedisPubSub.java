package com.github.paganini2008.springplayer.redis.pubsub;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * RedisPubSub
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisPubSub {

	String value();

	boolean repeatable() default true;

}
