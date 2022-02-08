package com.github.paganini2008.springplayer.gateway;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

import com.github.paganini2008.devtools.Env;
import com.github.paganini2008.devtools.io.FileUtils;
import com.github.paganini2008.springplayer.feign.EnableFeignClientEndpoint;

/**
 * 
 * GatewayApplication
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@EnableAsync
@EnableFeignClientEndpoint
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

	static {
		System.setProperty("spring.devtools.restart.enabled", "false");
		System.setProperty("reactor.netty.pool.leasingStrategy", "lifo");
		
		File logDir = FileUtils.getFile(FileUtils.getUserDirectory(), "logs", "spring-player-gateway-service");
		if (!logDir.exists()) {
			logDir.mkdirs();
		}
		System.setProperty("LOG_BASE", logDir.getAbsolutePath());
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
		System.out.println("PID: " + Env.getPid());
	}

}
