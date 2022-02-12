package com.github.paganini2008.springplayer.channel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.springplayer.channel.pojo.TemplateDTO;
import com.github.paganini2008.springplayer.channel.service.TemplateManagerService;
import com.github.paganini2008.springplayer.common.ApiResult;

/**
 * 
 * TemplateManagerController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Validated
@RestController
@RequestMapping("/template")
public class TemplateManagerController {

	@Autowired
	private TemplateManagerService templateManagerService;

	@PostMapping("/save")
	public ApiResult<String> saveTemplate(@Validated @RequestBody TemplateDTO dto) {
		templateManagerService.saveTemplate(dto);
		return ApiResult.ok("保存成功");
	}

	@DeleteMapping("/delete/{id}")
	public ApiResult<String> deleteTemplate(@PathVariable("id") long id) {
		templateManagerService.deleteById(id);
		return ApiResult.ok("删除成功");
	}

}
