package com.github.paganini2008.springplayer.gateway.sentinel;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import com.alibaba.cloud.sentinel.custom.SentinelAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.config.SentinelConfig;

/**
 * 
 * GatewaySentinelPublishConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ConditionalOnProperty("spring.cloud.gateway.sentinel.enabled")
@ConditionalOnClass({ SentinelConfig.class, SentinelAutoConfiguration.class })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Configuration(proxyBeanMethods = false)
public class GatewaySentinelPublishConfig {

	private final List<ViewResolver> viewResolvers;

	private final ServerCodecConfigurer serverCodecConfigurer;

	public GatewaySentinelPublishConfig(ObjectProvider<List<ViewResolver>> viewResolversProvider,
			ServerCodecConfigurer serverCodecConfigurer) {
		this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
		this.serverCodecConfigurer = serverCodecConfigurer;
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
		return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
	}

	@Bean
	public GlobalBlockRequestHandler globalBlockRequestHandler() {
		return new GlobalBlockRequestHandler();
	}

	@Autowired
	public void initializeBlockHandlers(GlobalBlockRequestHandler blockRequestHandler) {
		GatewayCallbackManager.setBlockHandler(blockRequestHandler);
	}
}
