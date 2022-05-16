package com.github.paganini2008.springplayer.common.feign;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientPackageScanningRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import feign.Feign;

/**
 * 
 * FeignClientPackageScanningAutoConfiguration
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@ConditionalOnClass(Feign.class)
@Import(FeignClientPackageScanningRegistrar.class)
@AutoConfigureAfter(EnableFeignClients.class)
public class FeignClientPackageScanningAutoConfiguration {
}
