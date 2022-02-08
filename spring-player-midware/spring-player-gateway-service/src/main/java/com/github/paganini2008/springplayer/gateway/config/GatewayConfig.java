package com.github.paganini2008.springplayer.gateway.config;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.ErrorHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * GatewayConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class GatewayConfig {

	@Bean("testPredicate")
	public Predicate<Object> testPredicate() {
		return obj -> true;
	}

	@Bean
	public CorsWebFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedMethod("*");
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
		source.registerCorsConfiguration("/**", config);
		return new CorsWebFilter(source);
	}

	@Primary
	@Bean
	public ThreadPoolTaskExecutor applicationClusterTaskExecutor() {
		final int nThreads = Runtime.getRuntime().availableProcessors() * 2;
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(nThreads);
		taskExecutor.setMaxPoolSize(200);
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		taskExecutor.setThreadNamePrefix("yl-platform-gateway-threads-");
		return taskExecutor;
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingBean(TaskScheduler.class)
	public static class TaskSchedulerConfig {

		@Value("${spring.application.name}")
		private String applicationName;

		@Bean
		public ThreadPoolTaskScheduler applicationClusterTaskScheduler() {
			ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
			threadPoolTaskScheduler.setPoolSize(16);
			threadPoolTaskScheduler.setThreadNamePrefix("spring-player-gateway-scheduler-threads-");
			threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
			threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
			threadPoolTaskScheduler.setErrorHandler(defaultErrorHandler());
			return threadPoolTaskScheduler;
		}

		@Bean
		public ErrorHandler defaultErrorHandler() {
			return new DefaultErrorHandler();
		}

		@Slf4j
		public static class DefaultErrorHandler implements ErrorHandler {

			@Override
			public void handleError(Throwable e) {
				log.error(e.getMessage(), e);
			}

		}
	}

}
