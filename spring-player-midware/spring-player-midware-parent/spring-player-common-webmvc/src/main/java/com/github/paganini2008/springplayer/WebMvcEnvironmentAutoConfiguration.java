package com.github.paganini2008.springplayer;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * WebMvcEnvironmentAutoConfiguration
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ComponentScan("com.github.paganini2008.springplayer.webmvc")
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class WebMvcEnvironmentAutoConfiguration {
}
