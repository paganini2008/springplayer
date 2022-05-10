package com.github.paganini2008.springplayer.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

import com.github.paganini2008.devtools.Env;
import com.github.paganini2008.springplayer.common.feign.EnableFeignClientEndpoint;

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
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
		System.out.println("PID: " + Env.getPid());
	}

}
