package com.github.paganini2008.springplayer.common.sentinel.example;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.github.paganini2008.devtools.multithreads.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * SentinelExampleService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Service
public class SentinelExampleService {

	/**
	 * 测试Service
	 * 
	 * @param name
	 * @return
	 */
	@SentinelResource(value = "sayHello", blockHandler = "sayHelloBlockHandler")
	public String sayHello(String name) {
		return "hello: " + name;
	}

	/**
	 * 测试Service中抛异常
	 * 
	 * @param name
	 * @return
	 */
	@SentinelResource(value = "compareName", fallback = "compareNameFallback", blockHandler = "compareNameBlockHandler")
	public String compareName(String name) {
		if ("tomcat".equals(name)) {
			return "hello: " + name + "\tnow: " + LocalDateTime.now();
		}
		throw new IllegalArgumentException("Name mismatched. Input: " + name);
	}

	/**
	 * 测试执行时间过长
	 * 
	 * @param timeout
	 * @return
	 */
	@SentinelResource(value = "waitForLongTime", fallback = "waitForLongTimeFallback")
	public String waitForLongTime(long timeout) {
		ThreadUtils.randomSleep(1000, Math.min(10000, timeout));
		return UUID.randomUUID().toString();
	}

	public String waitForLongTimeFallback(long timeout) {
		log.info("waitForLongTime方法执行时间过长，请稍后重试");
		return "waitForLongTime方法执行时间过长，请稍后重试";
	}

	public String compareNameFallback(String name) {
		log.info("compareName方法服务异常，熔断降级, 请稍后重试! Name: " + name);
		return "compareName方法服务异常，熔断降级, 请稍后重试! Name: " + name;
	}

	public String compareNameBlockHandler(String name, BlockException e) {
		log.info("compareName方法访问过快，限流降级, 请稍后重试! Name: " + name);
		return "compareName方法访问过快，限流降级, 请稍后重试! Name: " + name;
	}

	public String sayHelloBlockHandler(String name, BlockException e) {
		log.info("sayHello方法访问过快，限流降级, 请稍后重试!");
		return "sayHello方法访问过快，限流降级, 请稍后重试!";
	}

}
