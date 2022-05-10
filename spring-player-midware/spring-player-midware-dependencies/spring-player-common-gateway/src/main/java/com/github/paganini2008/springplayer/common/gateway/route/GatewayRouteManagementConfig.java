package com.github.paganini2008.springplayer.common.gateway.route;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.github.paganini2008.springplayer.common.gateway.utils.JdbcInitializer;

/**
 * 
 * GatewayRouteManagementConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@AutoConfigureBefore(GatewayAutoConfiguration.class)
@MapperScan("com.github.paganini2008.springplayer.common.gateway.route.mapper")
@ComponentScan("com.github.paganini2008.springplayer.common.gateway.route")
@ConditionalOnProperty(name = "spring.cloud.gateway.route.management.enabled", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Configuration(proxyBeanMethods = false)
public class GatewayRouteManagementConfig {

	@Bean
	public RouteDefinitionRepository redisCachedRouteDefinitionWriter(RedisTemplate<String, Object> redisTemplate) {
		return new RedisCachedRouteDefinitionWriter(redisTemplate);
	}

	@Bean("ymlReader")
	public RouteConfigReader ymlReader() {
		return new YmlRouteConfigReader();
	}

	@Bean("jsonReader")
	public RouteConfigReader jsonReader() {
		return new JsonRouteConfigReader();
	}

	@Bean("routeManagementJdbcInitializer")
	public JdbcInitializer jdbcInitializer(DataSource dataSource) {
		final String[] tableNames = { "sys_route", "sys_route_file" };
		return new JdbcInitializer(dataSource, tableNames);
	}

}
