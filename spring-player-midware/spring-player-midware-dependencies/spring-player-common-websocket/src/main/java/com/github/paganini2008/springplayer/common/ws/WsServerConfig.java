package com.github.paganini2008.springplayer.common.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 
 * WsServerConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration
public class WsServerConfig {

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	@Bean
	public WsBeanConfigurator wsBeanConfigurator() {
		return new WsBeanConfigurator();
	}

	@Bean
	public WsHandlerContext wsHandlerContext() {
		return new WsHandlerContext();
	}

}
