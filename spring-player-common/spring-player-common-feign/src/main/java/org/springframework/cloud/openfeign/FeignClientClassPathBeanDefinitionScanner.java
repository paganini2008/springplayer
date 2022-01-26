package org.springframework.cloud.openfeign;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import lombok.Setter;

/**
 * 
 * FeignClientClassPathBeanDefinitionScanner
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class FeignClientClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

	public FeignClientClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
		super(registry, false);
	}

	@Setter
	private Environment environment;

	@Setter
	private ClassLoader beanClassLoader;

	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		addIncludeFilter(new AnnotationTypeFilter(FeignClient.class));
		Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
		if (beanDefinitionHolders.size() > 0) {
			processBeanDefinitions(beanDefinitionHolders);
		}
		return beanDefinitionHolders;
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
	}

	private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitionHolders) {
		final BeanDefinitionRegistry registry = getRegistry();
		ScannedGenericBeanDefinition beanDefinition;
		for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
			beanDefinition = ((ScannedGenericBeanDefinition) beanDefinitionHolder.getBeanDefinition());
			String className = beanDefinition.getMetadata().getClassName();
			Class<?> clazz;
			try {
				clazz = beanClassLoader.loadClass(className);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
			AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(clazz, FeignClient.class);
			if (attributes == null) {
				continue;
			}

			if (registry.containsBeanDefinition(className) || registry.containsBeanDefinition(beanDefinition.getBeanClassName())) {
				continue;
			}

			registerClientConfiguration(registry, getClientName(attributes), attributes.get("configuration"));

			validate(attributes);

			beanDefinition.getPropertyValues().add("url", getUrl(attributes));
			beanDefinition.getPropertyValues().add("path", getPath(attributes));
			String name = getName(attributes);
			beanDefinition.getPropertyValues().add("name", name);

			if (attributes.containsKey("contextId")) {
				String contextId = getContextId(attributes);
				beanDefinition.getPropertyValues().add("contextId", contextId);
			}

			beanDefinition.getPropertyValues().add("type", className);
			beanDefinition.getPropertyValues().add("decode404", attributes.get("decode404"));
			beanDefinition.getPropertyValues().add("fallback", attributes.get("fallback"));
			beanDefinition.getPropertyValues().add("fallbackFactory", attributes.get("fallbackFactory"));
			beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

			// has a default, won't be null
			boolean primary = (Boolean) attributes.get("primary");
			beanDefinition.setPrimary(primary);

			beanDefinition.setBeanClass(FeignClientFactoryBean.class);
		}
	}

	private void validate(Map<String, Object> attributes) {
		AnnotationAttributes annotation = AnnotationAttributes.fromMap(attributes);
		// This blows up if an aliased property is overspecified
		FeignClientsRegistrar.validateFallback(annotation.getClass("fallback"));
		FeignClientsRegistrar.validateFallbackFactory(annotation.getClass("fallbackFactory"));
	}

	private String getName(Map<String, Object> attributes) {
		String name = (String) attributes.get("serviceId");
		if (!StringUtils.hasText(name)) {
			name = (String) attributes.get("name");
		}
		if (!StringUtils.hasText(name)) {
			name = (String) attributes.get("value");
		}
		name = resolve(name);
		return FeignClientsRegistrar.getName(name);
	}

	private String getContextId(Map<String, Object> attributes) {
		String contextId = (String) attributes.get("contextId");
		if (!StringUtils.hasText(contextId)) {
			return getName(attributes);
		}

		contextId = resolve(contextId);
		return FeignClientsRegistrar.getName(contextId);
	}

	private String resolve(String value) {
		if (StringUtils.hasText(value)) {
			return this.environment.resolvePlaceholders(value);
		}
		return value;
	}

	private String getUrl(Map<String, Object> attributes) {
		String url = resolve((String) attributes.get("url"));
		return FeignClientsRegistrar.getUrl(url);
	}

	private String getPath(Map<String, Object> attributes) {
		String path = resolve((String) attributes.get("path"));
		return FeignClientsRegistrar.getPath(path);
	}

	@Nullable
	private String getQualifier(@Nullable Map<String, Object> client) {
		if (client == null) {
			return null;
		}
		String qualifier = (String) client.get("qualifier");
		if (StringUtils.hasText(qualifier)) {
			return qualifier;
		}
		return null;
	}

	@Nullable
	private String getClientName(@Nullable Map<String, Object> client) {
		if (client == null) {
			return null;
		}
		String value = (String) client.get("contextId");
		if (!StringUtils.hasText(value)) {
			value = (String) client.get("value");
		}
		if (!StringUtils.hasText(value)) {
			value = (String) client.get("name");
		}
		if (!StringUtils.hasText(value)) {
			value = (String) client.get("serviceId");
		}
		if (StringUtils.hasText(value)) {
			return value;
		}

		throw new IllegalStateException("Either 'name' or 'value' must be provided in @" + FeignClient.class.getSimpleName());
	}

	private void registerClientConfiguration(BeanDefinitionRegistry registry, Object name, Object configuration) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(FeignClientSpecification.class);
		builder.addConstructorArgValue(name);
		builder.addConstructorArgValue(configuration);
		registry.registerBeanDefinition(name + "." + FeignClientSpecification.class.getSimpleName(), builder.getBeanDefinition());
	}

}
