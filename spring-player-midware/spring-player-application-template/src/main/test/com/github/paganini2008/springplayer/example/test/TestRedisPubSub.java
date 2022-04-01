package com.github.paganini2008.springplayer.example.test;

import static com.github.paganini2008.springplayer.common.Constants.SERVER_PORT_END_WITH;
import static com.github.paganini2008.springplayer.common.Constants.SERVER_PORT_START_WITH;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.paganini2008.devtools.multithreads.ThreadUtils;
import com.github.paganini2008.devtools.net.NetUtils;
import com.github.paganini2008.springplayer.common.redis.pubsub.RedisPubSubService;
import com.github.paganini2008.springplayer.example.ExampleApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * TestRedisPubSub
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@FixMethodOrder
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ExampleApplication.class })
public class TestRedisPubSub {

	static {
		final int port = NetUtils.getRandomPort(SERVER_PORT_START_WITH, SERVER_PORT_END_WITH);
		System.setProperty("server.port", String.valueOf(port));
	}

	@Autowired
	private RedisPubSubService redisPubSubService;

	@Test
	public void test1() throws IOException {
		redisPubSubService.convertAndMulticast("testPubSub", "Hello World!");
		ThreadUtils.sleep(5000L);
		log.info("Test1 Over");
	}

	@Test
	public void test2() throws IOException {
		redisPubSubService.convertAndMulticast("testDelayPubSub", "After 15 sec, Hello World", 10, TimeUnit.SECONDS);
		ThreadUtils.sleep(15000L);
		log.info("Test2 Over");
	}

}
