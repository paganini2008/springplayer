package com.github.paganini2008.springplayer.id.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.id.IdGenerator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * IdController
 *
 * @author Fred Feng
 *
 * @version 2.0.5
 */
@Api(tags = "ID生成API")
@RequestMapping("/id")
@RestController
public class IdController {

	@Autowired
	private IdGenerator idGenerator;

	@ApiOperation(value = "下一个ID", notes = "下一个ID")
	@GetMapping("/next")
	public ApiResult<Long> getNextValue() {
		return ApiResult.ok(idGenerator.generateId());
	}

	@ApiOperation(value = "当前ID", notes = "当前ID")
	@GetMapping("/current")
	public ApiResult<Long> getCurrentValue() {
		return ApiResult.ok(idGenerator.currentId());
	}

}
