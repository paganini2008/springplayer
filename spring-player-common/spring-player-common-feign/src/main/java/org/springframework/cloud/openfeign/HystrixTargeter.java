/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.openfeign;

import org.springframework.util.StringUtils;

import feign.Feign;
import feign.Target;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;

/**
 * @author Spencer Gibb
 * @author Erik Kringen
 */
@SuppressWarnings("unchecked")
public class HystrixTargeter implements Targeter {

	@Override
	public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign,
			FeignContext context, Target.HardCodedTarget<T> target) {
		if (!(feign instanceof feign.hystrix.HystrixFeign.Builder)) {
			return feign.target(target);
		}
		feign.hystrix.HystrixFeign.Builder builder = (feign.hystrix.HystrixFeign.Builder) feign;
		String name = StringUtils.isEmpty(factory.getContextId()) ? factory.getName()
				: factory.getContextId();
		SetterFactory setterFactory = getOptional(name, context, SetterFactory.class);
		if (setterFactory != null) {
			builder.setterFactory(setterFactory);
		}
		Class<?> fallback = factory.getFallback();
		if (fallback != void.class) {
			return targetWithFallback(name, context, target, builder, fallback);
		}
		Class<?> fallbackFactory = factory.getFallbackFactory();
		if (fallbackFactory != void.class) {
			return targetWithFallbackFactory(name, context, target, builder, fallbackFactory);
		}

		return feign.target(target);
	}

	protected <T> T targetWithFallbackFactory(String feignClientName, FeignContext context,
			Target.HardCodedTarget<T> target, HystrixFeign.Builder builder,
			Class<?> fallbackFactoryClass) {
		if (GenericTypeFallbackFactory.class.isAssignableFrom(fallbackFactoryClass)) {
			return builder.target(target, genericTypeFallbackFactory(target.type()));
		}
		FallbackFactory<? extends T> fallbackFactory = (FallbackFactory<? extends T>) getFromContext(
				"fallbackFactory", feignClientName, context, fallbackFactoryClass,
				FallbackFactory.class);
		return builder.target(target, fallbackFactory);
	}

	protected <T> GenericTypeFallbackFactory<T> genericTypeFallbackFactory(Class<T> type) {
		return new GenericTypeFallbackFactory<T>(type);
	}

	protected <T> T targetWithFallback(String feignClientName, FeignContext context,
			Target.HardCodedTarget<T> target, HystrixFeign.Builder builder,
			Class<?> fallback) {
		T fallbackInstance = getFromContext("fallback", feignClientName, context,
				fallback, target.type());
		return builder.target(target, fallbackInstance);
	}

	private <T> T getFromContext(String fallbackMechanism, String feignClientName,
			FeignContext context, Class<?> beanType, Class<T> targetType) {
		Object fallbackInstance = context.getInstance(feignClientName, beanType);
		if (fallbackInstance == null) {
			throw new IllegalStateException(String.format(
					"No " + fallbackMechanism
							+ " instance of type %s found for feign client %s",
					beanType, feignClientName));
		}

		if (!targetType.isAssignableFrom(beanType)) {
			throw new IllegalStateException(String.format("Incompatible "
					+ fallbackMechanism
					+ " instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s",
					beanType, targetType, feignClientName));
		}
		return (T) fallbackInstance;
	}

	protected <T> T getOptional(String feignClientName, FeignContext context,
			Class<T> beanType) {
		return context.getInstance(feignClientName, beanType);
	}

}
