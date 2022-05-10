package com.github.paganini2008.springplayer.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.github.paganini2008.devtools.Env;
import com.github.paganini2008.springplayer.common.feign.EnableFeignClientEndpoint;

/**
 * 
 * OAuth2ServerApplication
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@EnableFeignClientEndpoint
@EnableEurekaClient
@SpringBootApplication
public class OAuth2ServerApplication {

	static {
		System.setProperty("spring.devtools.restart.enabled", "false");
	}

	public static void main(String[] args) {

		SpringApplication.run(OAuth2ServerApplication.class, args);
		System.out.println("PID: " + Env.getPid());
	}

}
