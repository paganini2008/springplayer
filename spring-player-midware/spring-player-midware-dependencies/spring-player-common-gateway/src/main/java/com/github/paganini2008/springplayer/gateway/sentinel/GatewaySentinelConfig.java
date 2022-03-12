package com.github.paganini2008.springplayer.gateway.sentinel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.github.paganini2008.springplayer.common.sentinel.EnableSentinelResource;

/**
 * 
 * GatewaySentinelConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@EnableSentinelResource
@MapperScan("com.github.paganini2008.springplayer.gateway.sentinel.mapper")
@ComponentScan("com.github.paganini2008.springplayer.gateway.sentinel")
@Configuration(proxyBeanMethods = false)
public class GatewaySentinelConfig {
}
