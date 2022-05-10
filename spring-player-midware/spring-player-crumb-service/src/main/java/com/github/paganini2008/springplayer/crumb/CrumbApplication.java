package com.github.paganini2008.springplayer.crumb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.github.paganini2008.devtools.Env;
import com.github.paganini2008.springplayer.common.feign.EnableFeignClientEndpoint;

/**
 * 
 * CrumbApplication
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@EnableFeignClientEndpoint
@EnableEurekaClient
@SpringBootApplication
public class CrumbApplication {

	static {
		System.setProperty("spring.devtools.restart.enabled", "false");
	}

	public static void main(String[] args) {
		SpringApplication.run(CrumbApplication.class, args);
		System.out.println("PID: " + Env.getPid());
	}
	
}
