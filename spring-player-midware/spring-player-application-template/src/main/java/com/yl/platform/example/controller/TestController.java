package com.yl.platform.example.controller;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.devtools.multithreads.ThreadUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.id.IdGenerator;
import com.github.paganini2008.springplayer.redis.RedisBloomFilter;
import com.github.paganini2008.springplayer.redis.lock.RedisSharedLock;
import com.github.paganini2008.springplayer.redis.pubsub.RedisPubSubService;
import com.yl.platform.example.feign.RemoteTestService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * TestController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@RequestMapping("/test")
@RestController
public class TestController {

	@Autowired
	private IdGenerator idGenerator;

	@Autowired
	private RedisBloomFilter redisBloomFilter;

	@Autowired
	private RedisSharedLock sharedLock;

	@Autowired
	private RedisPubSubService redisPubSubService;
	
	@Autowired
	private RemoteTestService remoteTestService;

	@GetMapping("/echo")
	public ApiResult<String> echo(@RequestParam("q") String q) {
		return ApiResult.ok(q);
	}

	@GetMapping("/id")
	public ApiResult<String> id(@RequestParam("n") int n) {
		long id;
		for (int i = 0; i < n; i++) {
			id = idGenerator.generateId();
			if (redisBloomFilter.mightContain(String.valueOf(id))) {
				log.info("重复id: " + id);
			}
			redisBloomFilter.put(String.valueOf(id));
		}
		return ApiResult.ok("ok");
	}

	@GetMapping("/lock")
	public ApiResult<String> lock(@RequestParam("timeout") long timeout, @RequestParam("q") String q) {
		sharedLock.acquire();
		try {
			ThreadUtils.randomSleep(1000, timeout);
			log.info("Q: " + q);
		} finally {
			sharedLock.release();
		}
		return ApiResult.ok("ok");
	}

	@GetMapping("/pubsub")
	public ApiResult<String> pubsub(@RequestParam("q") String q) {
		redisPubSubService.convertAndUnicast("testPubSub", Collections.singletonMap("q", q));
		return ApiResult.ok("ok");
	}

	@GetMapping("/pubsub2")
	public ApiResult<String> pubsub(@RequestParam("q") String q, @RequestParam("delay") int delay) {
		redisPubSubService.convertAndUnicast("testPubSub", Collections.singletonMap("q", q), delay, TimeUnit.SECONDS);
		return ApiResult.ok("ok");
	}

}
