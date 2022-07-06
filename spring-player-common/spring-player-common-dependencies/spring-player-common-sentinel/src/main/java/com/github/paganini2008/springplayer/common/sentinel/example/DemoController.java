package com.github.paganini2008.springplayer.common.sentinel.example;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.github.paganini2008.devtools.collection.MapUtils;
//
///**
// * 
// * DemoController
// *
// * @author Fred Feng
// * @version 1.0.0
// */
//@RestController
//@RequestMapping("/sentinel/test")
//public class DemoController {
//
//	@Autowired
//	private DemoResourceService demoResourceService;
//
//	@PostMapping("/login")
//	public Map<String, String> login(@RequestBody LoginDTO dto) {
//		String token = demoResourceService.login(dto);
//		return MapUtils.toMap("msg", "登录成功", "token", token);
//	}
//
//	@GetMapping("/getUserInfo")
//	public Map<String, String> getUserInfo(@RequestParam("username") String username) {
//		return demoResourceService.getUserInfo(username);
//	}
//
//}
