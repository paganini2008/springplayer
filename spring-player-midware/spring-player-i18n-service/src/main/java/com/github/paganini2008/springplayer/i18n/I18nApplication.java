package com.github.paganini2008.springplayer.i18n;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.github.paganini2008.devtools.Env;

/**
 * 
 * I18nApplication
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@EnableEurekaClient
@SpringBootApplication
public class I18nApplication {

	static {
		System.setProperty("spring.devtools.restart.enabled", "false");
	}

	public static void main(String[] args) {
		
		SpringApplication.run(I18nApplication.class, args);
		System.out.println("PID: " + Env.getPid());
	}

}
