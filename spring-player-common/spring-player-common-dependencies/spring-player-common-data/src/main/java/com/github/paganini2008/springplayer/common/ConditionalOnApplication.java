package com.github.paganini2008.springplayer.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * 
 * ConditionalOnApplication
 *
 * @author Fred Feng
 *
 * @version 2.0.5
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnApplicationCondition.class)
public @interface ConditionalOnApplication {

	String[] value();

}
