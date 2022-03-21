package com.github.paganini2008.springplayer.file.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.paganini2008.springplayer.common.minio.MinIoFileStore;
import com.github.paganini2008.springplayer.common.minio.MinIoProperties;
import com.github.paganini2008.springplayer.common.minio.MinIoTemplate;

/**
 * 
 * FileStoreConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class FileStoreConfig {

	@ConditionalOnProperty(name = "spring.player.file.store", havingValue = "minio", matchIfMissing = true)
	@EnableConfigurationProperties(MinIoProperties.class)
	@Configuration(proxyBeanMethods = false)
	public static class MinIoFileStoreConfig {

		@Bean
		public MinIoTemplate minIoTemplate(MinIoProperties config) {
			return new MinIoTemplate(config);
		}

		@Bean
		public MinIoFileStore minIoFileStore(MinIoTemplate template) {
			return new MinIoFileStore(template);
		}

	}

}
