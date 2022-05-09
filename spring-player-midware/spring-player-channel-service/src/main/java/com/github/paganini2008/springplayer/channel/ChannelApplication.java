package com.github.paganini2008.springplayer.channel;

import static com.github.paganini2008.springplayer.common.Constants.SERVER_PORT_END_WITH;
import static com.github.paganini2008.springplayer.common.Constants.SERVER_PORT_START_WITH;

import java.io.File;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

import com.github.paganini2008.devtools.io.FileUtils;
import com.github.paganini2008.devtools.net.NetUtils;
import com.github.paganini2008.springplayer.common.feign.EnableFeignClientEndpoint;

/**
 * 
 * ChannelApplication
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@MapperScan("com.github.paganini2008.springplayer.channel.mapper")
@EnableFeignClientEndpoint
@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
public class ChannelApplication {

	static {
		System.setProperty("spring.devtools.restart.enabled", "false");
		File logDir = FileUtils.getFile(FileUtils.getUserDirectory(), "logs", "spring-player-channel-service");
		if (!logDir.exists()) {
			logDir.mkdirs();
		}
		System.setProperty("LOG_BASE", logDir.getAbsolutePath());
	}

	public static void main(String[] args) {
		final int port = NetUtils.getRandomPort(SERVER_PORT_START_WITH, SERVER_PORT_END_WITH);
		System.setProperty("server.port", String.valueOf(port));
		SpringApplication.run(ChannelApplication.class, args);
	}

}
