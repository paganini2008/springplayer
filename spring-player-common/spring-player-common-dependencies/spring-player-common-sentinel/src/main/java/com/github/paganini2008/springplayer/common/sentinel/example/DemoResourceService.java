package com.github.paganini2008.springplayer.common.sentinel.example;
//package com.yl.platform.sentinel.example;
//
//import java.util.Map;
//import java.util.UUID;
//
//import org.springframework.stereotype.Service;
//
//import com.alibaba.csp.sentinel.annotation.SentinelResource;
//import com.github.paganini2008.devtools.collection.MapUtils;
//
//import lombok.Data;
//
///**
// * 
// * DemoResourceService
// *
// * @author Fred Feng
// * @version 1.0.0
// */
//@SentinelResource(value = "demoResourceService", fallbackClass = DemoResourceServiceFallback.class, blockHandlerClass = DemoResourceServiceFallback.class)
//@Service
//public class DemoResourceService {
//
//	@Data
//	public static class LoginDTO {
//
//		private String username;
//		private String password;
//	}
//
//	public String login(LoginDTO dto) {
//		if ("admin".equals(dto.getUsername()) && "123456".equals(dto.getPassword())) {
//			return UUID.randomUUID().toString();
//		}
//		throw new IllegalStateException("登陆失败");
//	}
//
//	public Map<String, String> getUserInfo(String username) {
//		if ("admin".equals(username)) {
//			return MapUtils.toMap("username", "admin", "password", "123456");
//		}
//		return MapUtils.emptyMap();
//	}
//
//}
