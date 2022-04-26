package com.github.paganini2008.springplayer.common.gateway.sentinel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * GatewaySentinelConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@MapperScan("com.github.paganini2008.springplayer.gateway.sentinel.mapper")
@ComponentScan("com.github.paganini2008.springplayer.gateway.sentinel")
@Configuration(proxyBeanMethods = false)
public class GatewaySentinelConfig {
}