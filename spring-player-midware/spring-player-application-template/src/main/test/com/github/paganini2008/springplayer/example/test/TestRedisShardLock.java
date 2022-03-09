package com.github.paganini2008.springplayer.example.test;

import static com.github.paganini2008.springplayer.common.Constants.SERVER_PORT_END_WITH;
import static com.github.paganini2008.springplayer.common.Constants.SERVER_PORT_START_WITH;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.paganini2008.devtools.multithreads.ThreadUtils;
import com.github.paganini2008.devtools.net.NetUtils;
import com.github.paganini2008.springplayer.common.redis.lock.RedisSharedLock;
import com.github.paganini2008.springplayer.example.Application;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * TestRedisShardLock
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@FixMethodOrder
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
public class TestRedisShardLock {
	
	static {
		final int port = NetUtils.getRandomPort(SERVER_PORT_START_WITH, SERVER_PORT_END_WITH);
		System.setProperty("server.port", String.valueOf(port));
	}

	@Value("${server.port}")
	private int port;
	
	@Autowired
	private RedisSharedLock lock;

	@Test
	public void test() {
		ThreadUtils.loop(8, 1000, i -> {
			log.info("[{}] i: {}正在处理", port, i);
			boolean locked = true;
			try {
				if (locked = lock.acquire(5, TimeUnit.SECONDS)) {
					ThreadUtils.sleep(5, TimeUnit.SECONDS);
					log.info("ThreadName: {}, Do something at now: {}", Thread.currentThread().getName(), LocalDateTime.now());
				}
			} finally {
				if (locked) {
					lock.release();
				}
			}
		});
	}

}
