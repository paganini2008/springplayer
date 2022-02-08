package com.github.paganini2008.springplayer.sentinel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.sentinel.model.SentinelRule;
import com.github.paganini2008.springplayer.sentinel.pojo.SentinelRuleDTO;
import com.github.paganini2008.springplayer.sentinel.service.SentinelRuleManagerService;
import com.github.paganini2008.springplayer.sentinel.service.SentinelRulePublishService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * SentinelRuleController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@CrossOrigin(origins = "*")
@Validated
@RequestMapping("/sentinel")
@RestController
public class SentinelRuleController {

	@Autowired
	private SentinelRuleManagerService sentinelRuleManagerService;

	@Autowired(required = false)
	private SentinelRulePublishService sentinelRulePublishService;

	@PostMapping("/rule/save")
	public ApiResult<String> saveSentinelRule(@Validated @RequestBody SentinelRuleDTO dto) {
		sentinelRuleManagerService.saveSentinelRule(dto);
		return ApiResult.ok(dto.getRuleType().getRepr() + "保存成功");
	}

	@DeleteMapping("/rule/delete/{id}")
	public ApiResult<String> deleteSentinelRuleById(@PathVariable Long id) {
		sentinelRuleManagerService.deleteSentinelRuleById(id);
		return ApiResult.ok("删除成功");
	}

	@GetMapping("/rules")
	public ApiResult<List<SentinelRule>> findSentinelRule() {
		return ApiResult.ok(sentinelRuleManagerService.findSentinelRule());
	}

	@GetMapping("/rule/{id}")
	public ApiResult<SentinelRule> getSentinelRule(@PathVariable Long id) {
		return ApiResult.ok(sentinelRuleManagerService.getSentinelRuleById(id));
	}

	@PostMapping("/rule/publish")
	public ApiResult<String> publishSentinelRule() {
		sentinelRulePublishService.publishAllRuleTypes();
		return ApiResult.ok("规则发布成功");
	}

	@PostMapping("/rule/update/{id}")
	public ApiResult<String> updateSentinelRule(@PathVariable Long id) {
		sentinelRulePublishService.updateSentinelRule(id);
		return ApiResult.ok("规则更新成功");
	}

}
