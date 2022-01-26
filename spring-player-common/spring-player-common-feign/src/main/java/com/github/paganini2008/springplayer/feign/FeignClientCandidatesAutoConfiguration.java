
package com.github.paganini2008.springplayer.feign;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientCandidatesRegistrar;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import feign.Feign;

/**
 * 
 * FeignClientCandidatesAutoConfiguration
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@ConditionalOnClass(Feign.class)
@Import(FeignClientCandidatesRegistrar.class)
@AutoConfigureAfter(EnableFeignClients.class)
public class FeignClientCandidatesAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnAvailableEndpoint
	public FeignClientEndpoint feignClientEndpoint(ApplicationContext context) {
		return new FeignClientEndpoint(context);
	}

}
