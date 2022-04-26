package com.github.paganini2008.springplayer.common.sentinel.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * SentinelResourceController
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/sentinel/example")
public class SentinelResourceController {

	@Autowired
	private SentinelExampleService sentinelExampleService;

	@GetMapping("/selectOrderList")
	public Map<String, Object> selectOrderList(@RequestParam("n") int n, @RequestParam("origin") String origin) {
		ContextUtil.getContext().setOrigin(origin);
		Entry entry = null;
		try {
			entry = SphU.entry("selectOrderList");
			List<String> orderList = new ArrayList<>();
			for (int i = 1; i <= n; i++) {
				orderList.add("Order-" + i);
			}
			return Collections.singletonMap("data", orderList);
		} catch (BlockException ex) {
			return Collections.singletonMap("msg", "无权访问selectOrderList");
		} finally {
			if (entry != null) {
				entry.exit();
			}
			ContextUtil.exit();
		}
	}
	
	@GetMapping("/selectProductList")
	public Map<String, Object> selectProductList(@RequestParam("n") int n, @RequestParam("origin") String origin) {
		ContextUtil.getContext().setOrigin(origin);
		Entry entry = null;
		try {
			entry = SphU.entry("selectProductList");
			List<String> orderList = new ArrayList<>();
			for (int i = 1; i <= n; i++) {
				orderList.add("Product-" + i);
			}
			return Collections.singletonMap("data", orderList);
		} catch (BlockException ex) {
			return Collections.singletonMap("msg", "无权访问selectProductList");
		} finally {
			if (entry != null) {
				entry.exit();
			}
			ContextUtil.exit();
		}
	}

	@GetMapping("/waitForLongTime")
	public Map<String, Object> waitForLongTime(@RequestParam("timeout") long timeout) {
		String msg = sentinelExampleService.waitForLongTime(timeout);
		return Collections.singletonMap("msg", msg);
	}

	@GetMapping("/sayHello/{name}")
	public Map<String, Object> sayHello(@PathVariable String name) {
		String msg = sentinelExampleService.sayHello(name);
		return Collections.singletonMap("msg", msg);
	}

	@GetMapping("/compareName/{name}")
	public Map<String, Object> compareName(@PathVariable String name) {
		String msg = sentinelExampleService.compareName(name);
		return Collections.singletonMap("msg", msg);
	}

	@PostMapping("/testHotProduct")
	public Map<String, Object> testHotProduct(@RequestParam("productId") Long productId,
			@RequestParam(name = "score", required = false) Integer score) {
		Entry entry = null;
		try {

			entry = SphU.entry("testHotProduct", EntryType.IN, 1, productId);
			Map<String, Object> detail = new HashMap<>();
			detail.put("id", productId);
			detail.put("productName", "Product_" + productId);
			return Collections.singletonMap("data", detail);
		} catch (BlockException e) {
			log.error(e.getMessage(), e);
			return testHotSpotBlockHandler(productId, score);
		} finally {
			if (entry != null) {
				entry.exit();
			}
		}
	}

	public Map<String, Object> testHotSpotBlockHandler(Long productId, Integer score) {
		return Collections.singletonMap("msg", "商品号：" + productId + "被频繁访问，请稍后再试！");
	}

}
