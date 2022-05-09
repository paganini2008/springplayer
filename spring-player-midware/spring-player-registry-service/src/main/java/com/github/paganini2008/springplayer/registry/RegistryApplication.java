package com.github.paganini2008.springplayer.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import com.github.paganini2008.devtools.Env;

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
	}

	public static void main(String[] args) {
		SpringApplication.run(RegistryApplication.class, args);
		System.out.println("PID: " + Env.getPid());
	}

}
