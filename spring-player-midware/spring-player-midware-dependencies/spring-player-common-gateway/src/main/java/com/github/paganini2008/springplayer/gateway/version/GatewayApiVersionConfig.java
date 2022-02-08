package com.github.paganini2008.springplayer.gateway.version;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * GatewayApiVersionConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty("spring.cloud.gateway.apiversion.enabled")
public class GatewayApiVersionConfig {

	@Bean
	public GlobalFilter multiVersionRequestHeaderFilter() {
		return new MultiVersionRequestHeaderFilter();
	}

	@Bean
	public GlobalFilter multiVersionRequestParamFilter() {
		return new MultiVersionRequestParamFilter();
	}

}
