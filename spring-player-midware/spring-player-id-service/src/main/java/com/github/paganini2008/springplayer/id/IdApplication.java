package com.github.paganini2008.springplayer.id;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.github.paganini2008.devtools.Env;
import com.github.paganini2008.springplayer.common.swagger.EnableSwaggerResource;

/**
 * 
 * IdApplication
 *
 * @author Fred Feng
 *
 * @version 2.0.5
 */
@EnableSwaggerResource
@EnableDiscoveryClient
@SpringBootApplication
public class IdApplication {

	static {
		System.setProperty("spring.devtools.restart.enabled", "false");
	}

	public static void main(String[] args) {
		SpringApplication.run(IdApplication.class, args);
		System.out.println("PID: " + Env.getPid());
	}

}
