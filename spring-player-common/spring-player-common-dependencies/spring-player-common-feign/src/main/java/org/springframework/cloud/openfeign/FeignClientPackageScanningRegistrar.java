package org.springframework.cloud.openfeign;

import java.util.List;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;

import com.github.paganini2008.springplayer.common.feign.FeignClientCandidatesExcludedAutoConfiguration;
import com.github.paganini2008.springplayer.common.feign.FeignClientPackageScanningAutoConfiguration;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * FeignClientPackageScanningRegistrar
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class FeignClientPackageScanningRegistrar
		implements ImportBeanDefinitionRegistrar, BeanClassLoaderAware, ResourceLoaderAware, EnvironmentAware {

	@Getter
	@Setter
	private ClassLoader beanClassLoader;

	@Getter
	@Setter
	private Environment environment;

	@Getter
	@Setter
	private ResourceLoader resourceLoader;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
		registerFeignClients(registry);
	}

	private void registerFeignClients(BeanDefinitionRegistry registry) {
		List<String> packageNames = SpringFactoriesLoader.loadFactoryNames(FeignClientPackageScanningAutoConfiguration.class,
				beanClassLoader);
		if (packageNames.isEmpty()) {
			return;
		}
		FeignClientClassPathBeanDefinitionScanner classPathBeanDefinitionScanner = new FeignClientClassPathBeanDefinitionScanner(registry);
		if (resourceLoader != null) {
			classPathBeanDefinitionScanner.setResourceLoader(resourceLoader);
		}
		classPathBeanDefinitionScanner.setEnvironment(environment);
		classPathBeanDefinitionScanner.setBeanClassLoader(beanClassLoader);
		List<String> excludedClassNames = SpringFactoriesLoader.loadFactoryNames(FeignClientCandidatesExcludedAutoConfiguration.class,
				beanClassLoader);
		if (!CollectionUtils.isEmpty(excludedClassNames)) {
			classPathBeanDefinitionScanner.addExcludeFilter(new ClassNameTypeFilter(excludedClassNames));
		}
		classPathBeanDefinitionScanner.scan(packageNames.toArray(new String[0]));
	}

}
