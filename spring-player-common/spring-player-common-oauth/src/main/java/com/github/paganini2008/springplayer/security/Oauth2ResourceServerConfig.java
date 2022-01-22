package com.github.paganini2008.springplayer.security;

import java.io.IOException;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.github.paganini2008.springplayer.i18n.api.RemoteI18nService;

/**
 * 
 * Oauth2ResourceServerConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ComponentScan("com.github.paganini2008.springplayer.security")
@Configuration
public class Oauth2ResourceServerConfig {

	@Bean("lbRestTemplate")
	@LoadBalanced
	public RestTemplate lbRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				if (response.getRawStatusCode() != 400) {
					super.handleError(response);
				}
			}
		});
		return restTemplate;
	}
	
	@Bean
	public ErrorMessageSource errorMessageSource(RemoteI18nService remoteI18nService) {
		return new ErrorMessageSource(remoteI18nService);
	}

}
