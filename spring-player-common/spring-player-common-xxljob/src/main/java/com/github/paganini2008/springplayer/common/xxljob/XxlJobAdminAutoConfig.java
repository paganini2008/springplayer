package com.github.paganini2008.springplayer.common.xxljob;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * XxlJobAdminAutoConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@EnableConfigurationProperties(XxlJobProperties.class)
@Configuration(proxyBeanMethods = false)
public class XxlJobAdminAutoConfig {

	@Bean
	public XxlJobAdmin xxlJobAdmin(XxlJobProperties config) {
		return new XxlJobAdmin(config);
	}

}
