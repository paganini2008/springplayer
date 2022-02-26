package com.github.paganini2008.springplayer.gateway.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * SwaggerProperties
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("spring.application.swagger")
public class SwaggerProperties {

	private List<String> resourceProviders = new ArrayList<>();

}
