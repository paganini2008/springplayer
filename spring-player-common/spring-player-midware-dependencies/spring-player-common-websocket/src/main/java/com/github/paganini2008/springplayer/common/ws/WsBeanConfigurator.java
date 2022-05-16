package com.github.paganini2008.springplayer.common.ws;

import javax.websocket.server.ServerEndpointConfig;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * WsBeanConfigurator
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class WsBeanConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
		return applicationContext.getBean(clazz);
	}

}
