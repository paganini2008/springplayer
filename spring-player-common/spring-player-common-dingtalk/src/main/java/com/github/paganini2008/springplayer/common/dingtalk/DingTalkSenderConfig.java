package com.github.paganini2008.springplayer.common.dingtalk;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.paganini2008.springplayer.common.dingtalk.template.DingTalkTemplate;
import com.github.paganini2008.springplayer.common.dingtalk.template.TextDingTalkTemplate;

/**
 * 
 * DingTalkSenderConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class DingTalkSenderConfig {

	@ConditionalOnMissingBean
	@Bean
	public DingTalkTemplate dingTalkTemplate() {
		return new TextDingTalkTemplate();
	}

	@ConditionalOnMissingBean
	@Bean
	public DingTalkService dingTalkService() {
		return new DingTalkServiceImpl();
	}

}
