package com.github.paganini2008.springplayer.upms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.github.paganini2008.devtools.Env;
import com.github.paganini2008.springplayer.common.feign.EnableFeignClientEndpoint;
import com.github.paganini2008.springplayer.common.oauth.EnableResourceServerSecurity;

/**
 * 
 * UpmsApplication
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@MapperScan("com.github.paganini2008.springplayer.upms.mapper")
@EnableResourceServerSecurity
@EnableFeignClientEndpoint
@EnableEurekaClient
@SpringBootApplication
public class UpmsApplication {

	static {
		System.setProperty("spring.devtools.restart.enabled", "false");
	}

	public static void main(String[] args) {

		SpringApplication.run(UpmsApplication.class, args);
		System.out.println("PID: " + Env.getPid());
	}
}
