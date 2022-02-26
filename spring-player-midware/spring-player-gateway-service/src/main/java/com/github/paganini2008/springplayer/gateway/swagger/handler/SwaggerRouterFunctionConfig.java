
package com.github.paganini2008.springplayer.gateway.swagger.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import lombok.AllArgsConstructor;

/**
 * 
 * SwaggerRouterFunctionConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Configuration
@AllArgsConstructor
public class SwaggerRouterFunctionConfig {

	private final SwaggerResourceHandler swaggerResourceHandler;

	private final SwaggerSecurityHandler swaggerSecurityHandler;

	private final SwaggerUiHandler swaggerUiHandler;

	@Bean
	public RouterFunction<?> routerFunction() {
		return RouterFunctions
				.route(RequestPredicates.GET("/swagger-resources").and(RequestPredicates.accept(MediaType.ALL)), swaggerResourceHandler)
				.andRoute(RequestPredicates.GET("/swagger-resources/configuration/ui").and(RequestPredicates.accept(MediaType.ALL)),
						swaggerUiHandler)
				.andRoute(RequestPredicates.GET("/swagger-resources/configuration/security").and(RequestPredicates.accept(MediaType.ALL)),
						swaggerSecurityHandler);

	}

}
