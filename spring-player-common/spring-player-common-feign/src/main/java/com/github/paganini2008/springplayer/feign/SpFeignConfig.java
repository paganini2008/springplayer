package com.github.paganini2008.springplayer.feign;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.github.paganini2008.springplayer.common.Constants;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Retryer;
import feign.codec.Decoder;

/**
 * 
 * SpFeignConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration
public class SpFeignConfig {

	@Autowired
	private ObjectFactory<HttpMessageConverters> messageConverters;

	@Bean
	public Decoder feignDecoder() {
		return new ApiResultResponseEntityDecoder(new SpringDecoder(this.messageConverters));
	}

	@Bean
	public Retryer feignRetryer() {
		return Retryer.NEVER_RETRY;
	}

	@Bean
	public Logger.Level logger() {
		return Logger.Level.FULL;
	}

	@Order(0)
	@Bean
	public RequestInterceptor setTimestamp() {
		return new TimestampRequestInterceptor();
	}

	public static class TimestampRequestInterceptor implements RequestInterceptor {

		public void apply(RequestTemplate template) {
			template.header(Constants.TIMESTAMP, String.valueOf(System.currentTimeMillis()));
		}

	}

}
