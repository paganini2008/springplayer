package com.github.paganini2008.springplayer.registry;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import com.github.paganini2008.devtools.Env;
import com.github.paganini2008.devtools.io.FileUtils;

/**
 * 
 * RegistryApplication
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@EnableEurekaServer
@SpringBootApplication
public class RegistryApplication {

	static {
		System.setProperty("spring.devtools.restart.enabled", "false");

		File logDir = FileUtils.getFile(FileUtils.getUserDirectory(), "logs", "spring-player-registry-service");
		if (!logDir.exists()) {
			logDir.mkdirs();
		}
		System.setProperty("LOG_BASE", logDir.getAbsolutePath());
	}

	public static void main(String[] args) {
		SpringApplication.run(RegistryApplication.class, args);
		System.out.println("PID: " + Env.getPid());
	}

}
