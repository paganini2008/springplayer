package com.github.paganini2008.springplayer.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

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

	public static void main(String[] args) {
		SpringApplication.run(RegistryApplication.class, args);
	}

}
