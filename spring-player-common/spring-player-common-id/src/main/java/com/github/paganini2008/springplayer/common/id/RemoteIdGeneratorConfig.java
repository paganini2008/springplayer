package com.github.paganini2008.springplayer.common.id;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.paganini2008.springplayer.common.ConditionalOnExcludedApplication;

/**
 * 
 * RemoteIdGeneratorConfig
 *
 * @author Feng Yan
 *
 * @version 2.0.5
 */
@ConditionalOnExcludedApplication({ "yl-platform-id-service" })
@Configuration(proxyBeanMethods = false)
public class RemoteIdGeneratorConfig {

	@ConditionalOnMissingBean
	@Bean
	public IdGeneratorFactory idGeneratorFactory(RemoteIdService remoteIdService) {
		return new RemoteIdGeneratorFactory(remoteIdService);
	}

}
