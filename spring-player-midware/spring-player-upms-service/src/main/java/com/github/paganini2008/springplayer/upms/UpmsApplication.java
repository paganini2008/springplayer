package com.github.paganini2008.springplayer.upms;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.github.paganini2008.devtools.Env;
import com.github.paganini2008.springplayer.security.EnableOauth2ResourceServer;

/**
 * 
 * UpmsApplication
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@MapperScan("com.github.paganini2008.springplayer.upms.mapper")
@EnableOauth2ResourceServer
@EnableEurekaClient
@SpringBootApplication
public class UpmsApplication {

	static {
		System.setProperty("spring.devtools.restart.enabled", "false");

		File logDir = FileUtils.getFile(FileUtils.getUserDirectory(), "logs", "spring-player-upms-service");
		if (!logDir.exists()) {
			logDir.mkdirs();
		}
		System.setProperty("LOG_BASE", logDir.getAbsolutePath());
	}

	public static void main(String[] args) {

		SpringApplication.run(UpmsApplication.class, args);
		System.out.println("PID: " + Env.getPid());
	}
}
