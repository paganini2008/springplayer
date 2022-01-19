package com.github.paganini2008.springplayer;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * WebFluxEnvironmentAutoConfiguration
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ComponentScan("com.github.paganini2008.springplayer.webflux")
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
public class WebFluxEnvironmentAutoConfiguration {
}
