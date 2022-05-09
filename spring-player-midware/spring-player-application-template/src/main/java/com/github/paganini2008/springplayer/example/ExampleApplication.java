package com.github.paganini2008.springplayer.example;

import static com.github.paganini2008.springplayer.common.Constants.SERVER_PORT_END_WITH;
import static com.github.paganini2008.springplayer.common.Constants.SERVER_PORT_START_WITH;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.devtools.net.NetUtils;
import com.github.paganini2008.springplayer.common.feign.EnableFeignClientEndpoint;

/**
 * 
 * Application
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RestController
@MapperScan("com.github.paganini2008.springplayer.*.mapper")
@EnableFeignClientEndpoint
@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
public class ExampleApplication {

	public static void main(String[] args) {
		final int port = NetUtils.getRandomPort(SERVER_PORT_START_WITH, SERVER_PORT_END_WITH);
		// int port = 9092;
		System.setProperty("server.port", String.valueOf(port));
		SpringApplication.run(ExampleApplication.class, args);
	}

	@GetMapping("/")
	public String helloWorld() {
		return "Hello World";
	}
}
