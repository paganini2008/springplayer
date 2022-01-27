package com.github.paganini2008.springplayer.email;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

/**
 * 
 * DisableViewResolverPostHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class DisableViewResolverPostHandler implements EnvironmentPostProcessor{

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		MutablePropertySources propertySources = environment.getPropertySources();
		Map<String, Object> config = new HashMap<>();
		config.put("spring.freemarker.enabled", "false");
		config.put("spring.thymeleaf.enabled", "false");
		propertySources.addFirst(new MapPropertySource("emailExtraConfig", config));
	}

}
