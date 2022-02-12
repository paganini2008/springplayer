package com.github.paganini2008.springplayer.channel;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

import com.github.paganini2008.springplayer.feign.EnableFeignClientEndpoint;

/**
 * 
 * ChannelApplication
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@EnableFeignClientEndpoint
@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
public class ChannelApplication {

	public static void main(String[] args) {
		

	}

}
