package com.github.paganini2008.springplayer.common.gateway.version;

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
@ConditionalOnProperty("spring.cloud.gateway.api-version.enabled")
@Configuration(proxyBeanMethods = false)
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
