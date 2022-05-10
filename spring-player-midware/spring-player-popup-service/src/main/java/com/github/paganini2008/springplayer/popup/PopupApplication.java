package com.github.paganini2008.springplayer.popup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.github.paganini2008.devtools.Env;
import com.github.paganini2008.springplayer.common.ws.EnableWsServer;

/**
 * 
 * PopupApplication
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@EnableWsServer
@EnableEurekaClient
@SpringBootApplication
public class PopupApplication {
	
	static {
		System.setProperty("spring.devtools.restart.enabled", "false");
	}

	public static void main(String[] args) {
		SpringApplication.run(PopupApplication.class, args);
		System.out.println("PID: " + Env.getPid());
	}

}
