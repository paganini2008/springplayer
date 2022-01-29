package com.github.paganini2008.springplayer.es;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * EsProperties
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "spring.elasticsearch")
@Getter
@Setter
public class EsProperties {

	private String host;
	private int port;
	private int connectTimeout;
	private int socketTimeout;
	private int connectionRequestTimeout;
	private String username;
	private String password;

}
