package com.github.paganini2008.springplayer.common.webmvc;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.github.paganini2008.springplayer.common.context.MessageLocalization;
import com.github.paganini2008.springplayer.common.context.NoopMessageLocalization;

/**
 * 
 * CommonWebMvcConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ComponentScan("com.github.paganini2008.springplayer.common.webmvc")
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@Configuration
public class BasicWebMvcConfig {

	@ConditionalOnProperty("spring.application.cors.enabled")
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("UTF-8");
		multipartResolver.setMaxUploadSize(DataSize.ofMegabytes(100).toBytes());
		return multipartResolver;
	}

	@ConditionalOnMissingBean
	@Bean
	public MessageLocalization messageLocalization() {
		return new NoopMessageLocalization();
	}

}
