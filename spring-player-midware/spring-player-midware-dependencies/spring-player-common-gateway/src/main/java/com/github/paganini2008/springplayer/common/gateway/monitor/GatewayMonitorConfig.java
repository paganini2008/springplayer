package com.github.paganini2008.springplayer.common.gateway.monitor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * GatewayMonitorConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "spring.cloud.gateway.monitor.enabled", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class GatewayMonitorConfig {

	@Bean
	public ApiWatcherContext apiWatcherContext() {
		return new ApiWatcherContext();
	}

	@Bean
	public GlobalFilter apiWatcherFilter() {
		return new ApiWatcherFilter();
	}

	@Bean
	public GlobalFilter errorGlobalFilter() {
		return new ErrorGlobalFilter();
	}

}
