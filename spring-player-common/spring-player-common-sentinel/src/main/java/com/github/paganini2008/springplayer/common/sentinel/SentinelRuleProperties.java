package com.github.paganini2008.springplayer.common.sentinel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * SentinelRuleProperties
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("spring.cloud.sentinel.rule")
public class SentinelRuleProperties {

	private List<String> authorityRuleKeys = new ArrayList<>();
	private List<String> degradeRuleKeys = new ArrayList<>();
	private List<String> systemRuleKeys = new ArrayList<>();
	private List<String> flowRuleKeys = new ArrayList<>();
	private List<String> paramFlowRuleKeys = new ArrayList<>();

}
