package com.github.paganini2008.springplayer.swagger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * EnableSwaggerResource
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
@Documented
@EnableKnife4j
@EnableSwagger2
@Import(SwaggerResourceConfig.class)
public @interface EnableSwaggerResource {

}
