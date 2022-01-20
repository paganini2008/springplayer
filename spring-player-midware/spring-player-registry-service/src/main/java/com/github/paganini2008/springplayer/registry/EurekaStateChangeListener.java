package com.github.paganini2008.springplayer.registry;

import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaRegistryAvailableEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * EurekaStateChangeListener
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@Slf4j
@Component
public class EurekaStateChangeListener {

	@EventListener
	public void listen(EurekaInstanceCanceledEvent e) {
		log.info(e.toString());
	}

	@EventListener
	public void listen(EurekaInstanceRegisteredEvent e) {
		log.info(e.toString());
	}

	@EventListener
	public void listen(EurekaInstanceRenewedEvent e) {
		log.info(e.toString());
	}

	@EventListener
	public void listen(EurekaRegistryAvailableEvent e) {
		log.info(e.toString());
	}

	@EventListener
	public void listen(EurekaServerStartedEvent e) {
		log.info(e.toString());
	}

}
