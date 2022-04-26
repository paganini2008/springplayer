package com.github.paganini2008.springplayer.common.sentinel.example;

import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.github.paganini2008.devtools.RandomUtils;
import com.github.paganini2008.devtools.multithreads.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * SentinelExampleController
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/sentinel/example")
public class SentinelExampleController {

	/**
	 * 测试接口中报异常
	 * 
	 * @param number
	 * @return
	 */
	@SentinelResource(value = "testNumber", fallback = "testNumberFallback")
	@PostMapping("/testNumber/{number}")
	public Map<String, Object> testNumber(@PathVariable("number") long number) {
		long rand = RandomUtils.randomLong(number, Math.max(100, number));
		if ((rand & 1) == 0) {
			throw new IllegalArgumentException("Wrong number: " + rand);
		}
		return Collections.singletonMap("number", number);
	}

	/**
	 * 测试慢查询接口
	 * 
	 * @param q
	 * @return
	 */
	@SentinelResource(value = "testSlowQuery", blockHandler = "testSlowQueryBlockHandler")
	@PostMapping("/testSlowQuery")
	public Map<String, Object> testSlowQuery(@RequestParam("q") String q) {
		ThreadUtils.randomSleep(1000, 10000);
		return Collections.singletonMap("answer", q);
	}

	public Map<String, Object> testNumberFallback(long number, Throwable e) {
		String msg = "调用testNumber接口失败达到阈值，已被降级, number: " + number;
		log.info(msg);
		return Collections.singletonMap("msg", msg);
	}

	public Map<String, Object> testSlowQueryBlockHandler(String q, BlockException e) {
		String msg = "调用testSlowQuery接口过快达到阈值，已被限流, q: " + q;
		log.info(msg);
		return Collections.singletonMap("msg", msg);
	}

}
