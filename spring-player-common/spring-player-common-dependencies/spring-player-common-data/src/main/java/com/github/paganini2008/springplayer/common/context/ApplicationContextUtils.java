package com.github.paganini2008.springplayer.common.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.github.paganini2008.devtools.Assert;

/**
 * 
 * ApplicationContextUtils
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@SuppressWarnings("unchecked")
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

	private static final SpringContextHolder contextHolder = new SpringContextHolder();

	static class SpringContextHolder {

		ApplicationContext applicationContext;
		AutowireCapableBeanFactory beanFactory;
		Environment environment;

		public ApplicationContext getApplicationContext() {
			Assert.isNull(applicationContext, new IllegalStateException("Nullable ApplicationContext."));
			return applicationContext;
		}

		public AutowireCapableBeanFactory getBeanFactory() {
			Assert.isNull(beanFactory, new IllegalStateException("Nullable beanFactory."));
			return beanFactory;
		}

		public Environment getEnvironment() {
			Assert.isNull(environment, new IllegalStateException("Nullable environment."));
			return environment;
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		contextHolder.applicationContext = applicationContext;
		contextHolder.beanFactory = applicationContext.getAutowireCapableBeanFactory();
		contextHolder.environment = applicationContext.getEnvironment();
	}

	public static ApplicationContext getApplicationContext() {
		return contextHolder.getApplicationContext();
	}

	public static AutowireCapableBeanFactory getBeanFactory() {
		return contextHolder.getBeanFactory();
	}

	public static Environment getEnvironment() {
		return contextHolder.getEnvironment();
	}

	public static <T> T getBean(String name) {
		return (T) getApplicationContext().getBean(name);
	}

	public static <T> T getBean(Class<T> requiredType) {
		return getApplicationContext().getBean(requiredType);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return getApplicationContext().getBean(name, requiredType);
	}

	public static String[] getBeanNames() {
		return getApplicationContext().getBeanDefinitionNames();
	}

	public static <T> T autowireBean(T object) {
		getBeanFactory().autowireBean(object);
		return object;
	}

	public static String getRequiredProperty(String key) {
		return getEnvironment().getRequiredProperty(key);
	}

	public static <T> T getProperty(String key, Class<T> requiredType) {
		return getEnvironment().getProperty(key, requiredType);
	}

	public static <T> T getProperty(String key, Class<T> requiredType, T defaultValue) {
		return getEnvironment().getProperty(key, requiredType, defaultValue);
	}

	public static String getProperty(String key, String defaultValue) {
		return getEnvironment().getProperty(key, defaultValue);
	}

	public static String getProperty(String key) {
		return getEnvironment().getProperty(key);
	}

	public static <T extends ApplicationEvent> void publishEvent(T event) {
		getApplicationContext().publishEvent(event);
	}

}
