package com.github.paganini2008.springplayer.common.xxljob;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 
 * XxlJobProperties
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Data
@ConfigurationProperties("spring.player.xxljob")
public class XxlJobProperties {

	private String url = "http://localhost:8080/xxl-job-admin";
	private String username = "admin";
	private String password = "123456";

}
