package com.github.paganini2008.springplayer.gateway.sentinel.pojo;

import java.util.List;

import lombok.Data;

/**
 * 
 * GatewayFlowRuleDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Data
public class GatewayFlowRuleDTO {

	private String resource;
	private Integer resourceMode;

	private Integer grade;
	private Double count;
	private Long intervalSec;

	private Integer controlBehavior;
	private Integer burst;
	private Integer maxQueueingTimeoutMs;
	private List<ApiPathPredicateItemDTO> api;

}
