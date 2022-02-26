package com.github.paganini2008.springplayer.gateway.swagger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.github.paganini2008.springplayer.common.Constants;

import lombok.AllArgsConstructor;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * 
 * SwaggerResourcesConfig
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@EnableConfigurationProperties(SwaggerProperties.class)
@Component
@Primary
@AllArgsConstructor
public class SwaggerResourcesConfig implements SwaggerResourcesProvider {

	private static final String API_URI = "/v2/api-docs";
	private final RouteDefinitionRepository routeDefinitionRepository;
	private final SwaggerProperties swaggerProperties;

	@Override
	public List<SwaggerResource> get() {
		List<RouteDefinition> routes = new ArrayList<>();
		routeDefinitionRepository.getRouteDefinitions().subscribe(routes::add);
		return routes.stream()
				.flatMap(routeDefinition -> routeDefinition.getPredicates().stream()
						.filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
						.filter(predicateDefinition -> !swaggerProperties.getResourceProviders().contains(routeDefinition.getId()))
						.map(predicateDefinition -> createSwaggerResource(routeDefinition.getId(),
								predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**", API_URI))))
				.sorted(Comparator.comparing(SwaggerResource::getName)).collect(Collectors.toList());
	}

	private static SwaggerResource createSwaggerResource(String name, String location) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion(Constants.VERSION);
		return swaggerResource;
	}

}
