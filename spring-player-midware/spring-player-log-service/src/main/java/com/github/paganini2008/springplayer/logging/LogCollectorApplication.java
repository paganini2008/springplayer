package com.github.paganini2008.springplayer.logging;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.github.paganini2008.devtools.Env;

/**
 * 
 * LogCollectorApplication
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@EnableEurekaClient
@SpringBootApplication
public class LogCollectorApplication {

	static {
		System.setProperty("spring.devtools.restart.enabled", "false");
		
		File logDir = FileUtils.getFile(FileUtils.getUserDirectory(), "logs", "spring-player-log-service");
		if (!logDir.exists()) {
			logDir.mkdirs();
		}
		System.setProperty("LOG_BASE", logDir.getAbsolutePath());
	}

	public static void main(String[] args) {
		SpringApplication.run(LogCollectorApplication.class, args);
		System.out.println("PID: " + Env.getPid());
	}

}
