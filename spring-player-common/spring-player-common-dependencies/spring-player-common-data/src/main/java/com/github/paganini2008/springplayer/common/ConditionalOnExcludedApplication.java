package com.github.paganini2008.springplayer.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * 
 * ConditionalOnExcludedApplication
 *
 * @author Fred Feng
 *
 * @version 2.0.5
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnExcludedApplicationCondition.class)
public @interface ConditionalOnExcludedApplication {

	String[] value();
	
}
