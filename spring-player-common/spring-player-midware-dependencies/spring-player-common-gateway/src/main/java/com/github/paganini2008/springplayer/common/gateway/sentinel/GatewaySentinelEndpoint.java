package com.github.paganini2008.springplayer.common.gateway.sentinel;

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
import com.github.paganini2008.springplayer.common.gateway.sentinel.model.SentinelRule;
import com.github.paganini2008.springplayer.common.gateway.sentinel.pojo.SentinelRuleDTO;
import com.github.paganini2008.springplayer.common.gateway.sentinel.service.SentinelRuleManagerService;
import com.github.paganini2008.springplayer.common.gateway.sentinel.service.SentinelRulePublishService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 
 * GatewaySentinelEndpoint
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@CrossOrigin(origins = "*")
@Validated
@RequestMapping("/gateway-admin")
@RestController
public class GatewaySentinelEndpoint {

	@Autowired
	private SentinelRuleManagerService sentinelRuleManagerService;

	@Autowired(required = false)
	private SentinelRulePublishService sentinelRulePublishService;

	@PostMapping("/sentinel/rule/save")
	public Mono<ApiResult<String>> saveSentinelRule(@Validated @RequestBody SentinelRuleDTO dto) {
		sentinelRuleManagerService.saveSentinelRule(dto);
		return Mono.just(ApiResult.ok(dto.getRuleType().getRepr() + "保存成功"));
	}

	@DeleteMapping("/sentinel/rule/delete/{id}")
	public Mono<ApiResult<String>> deleteSentinelRuleById(@PathVariable Long id) {
		sentinelRuleManagerService.deleteSentinelRuleById(id);
		return Mono.just(ApiResult.ok("删除成功"));
	}

	@GetMapping("/sentinel/rules")
	public Mono<ApiResult<List<SentinelRule>>> findSentinelRule() {
		return Mono.just(ApiResult.ok(sentinelRuleManagerService.findSentinelRule()));
	}

	@GetMapping("/sentinel/rule/{id}")
	public Mono<ApiResult<SentinelRule>> getSentinelRule(@PathVariable Long id) {
		return Mono.just(ApiResult.ok(sentinelRuleManagerService.getSentinelRuleById(id)));
	}

	@PostMapping("/sentinel/rule/publish")
	public Mono<ApiResult<String>> publishSentinelRule() {
		checkSentinelEnabled();
		sentinelRulePublishService.publishAllRuleTypes();
		return Mono.just(ApiResult.ok("规则发布成功"));
	}

	@PostMapping("/sentinel/rule/update/{id}")
	public Mono<ApiResult<String>> updateSentinelRule(@PathVariable Long id) {
		checkSentinelEnabled();
		sentinelRulePublishService.updateSentinelRule(id);
		return Mono.just(ApiResult.ok("规则更新成功"));
	}

	private void checkSentinelEnabled() {
		if (sentinelRulePublishService == null) {
			throw new IllegalStateException("哨兵功能未开启");
		}
	}

}
