package com.github.paganini2008.springplayer.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.springplayer.common.ApiResult;

/**
 * 
 * ExampleController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RequestMapping("/test")
@RestController
public class ExampleController {

	@GetMapping("/echo")
	public ApiResult<String> echo(@RequestParam("q") String q) {
		return ApiResult.ok(q);
	}

}
