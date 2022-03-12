package com.github.paganini2008.springplayer.gateway.sentinel.pojo;

import javax.validation.constraints.NotNull;

import com.github.paganini2008.springplayer.common.sentinel.RuleType;
import com.github.paganini2008.springplayer.common.validation.JsonWellformed;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * SentinelRuleDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class SentinelRuleDTO {

	private Long id;

	@NotNull(message = "规则键值不能为空")
	private String ruleKey;

	@NotNull(message = "规则类型不能为空")
	private RuleType ruleType;

	@JsonWellformed(test = GatewayFlowRuleDTO[].class, message = "格式错误的JSON文本")
	private String rule;

}
