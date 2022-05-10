package com.github.paganini2008.springplayer.common.sentinel.example;

import com.alibaba.csp.sentinel.slots.block.BlockException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * DemoResourceServiceFallback
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class DemoResourceServiceFallback {

	public static String fallbackForLogin(String username, String password) {
		String msg = String.format("错误的用户名和密码：%s/%s", username, password);
		log.warn(msg);
		return msg;
	}

	public static String blockForLogin(String username, String password, BlockException e) {
		log.error("频繁的登陆操作：", e);
		return String.format("频繁的登陆操作：%s/%s", username, password);
	}

	public static String blockForGetUserInfo(String username, BlockException e) {
		return String.format("频繁的获取操作：%s", username);
	}

}
