package com.yl.platform.id.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.id.IdGenerator;

/**
 * 
 * IdController
 *
 * @author Feng Yan
 *
 * @version 2.0.5
 */
@RequestMapping("/id")
@RestController
public class IdController {

	@Autowired
	private IdGenerator idGenerator;

	@GetMapping("/next")
	public ApiResult<Long> getNextValue() {
		return ApiResult.ok(idGenerator.generateId());
	}

	@GetMapping("/current")
	public ApiResult<Long> getCurrentValue() {
		return ApiResult.ok(idGenerator.currentId());
	}

}
