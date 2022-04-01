package com.github.paganini2008.springplayer.messenger;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

import com.github.paganini2008.devtools.io.FileUtils;
import com.github.paganini2008.springplayer.common.swagger.EnableSwaggerResource;
import com.github.paganini2008.springplayer.common.ws.EnableWsClient;

/**
 * 
 * MessengerApplication
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@EnableWsClient
@EnableSwaggerResource
@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
public class MessengerApplication {

	static {
		System.setProperty("spring.devtools.restart.enabled", "false");
		File logDir = FileUtils.getFile(FileUtils.getUserDirectory(), "logs", "spring-player-messenger-service");
		if (!logDir.exists()) {
			logDir.mkdirs();
		}
		System.setProperty("LOG_BASE", logDir.getAbsolutePath());
	}

	public static void main(String[] args) {
		final int port = 12000;
		System.setProperty("server.port", String.valueOf(port));
		SpringApplication.run(MessengerApplication.class, args);
	}

}
