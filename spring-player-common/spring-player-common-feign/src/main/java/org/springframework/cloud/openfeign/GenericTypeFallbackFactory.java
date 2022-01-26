package org.springframework.cloud.openfeign;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import feign.hystrix.FallbackFactory;

/**
 * 
 * GenericTypeFallbackFactory
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class GenericTypeFallbackFactory<T> implements FallbackFactory<T>, MethodInterceptor {

	private static final Log log = LogFactory.getLog(GenericTypeFallbackFactory.class);
	private final Class<T> feignClientType;

	public GenericTypeFallbackFactory(Class<T> feignClientType) {
		this.feignClientType = feignClientType;
	}

	private T feignClientProxy;

	@Override
	public synchronized T create(Throwable e) {
		if (e != null) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage(), e);
			}
		}
		if (feignClientProxy == null) {
			feignClientProxy = createProxyObject();
		}
		return feignClientProxy;
	}

	@SuppressWarnings("unchecked")
	protected T createProxyObject() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(feignClientType);
		enhancer.setCallback(this);
		return (T) enhancer.create();
	}

	@Override
	public final Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		final String methodName = method.getName();
		if (methodName.equals("equals")) {
			return false;
		} else if (methodName.equals("hashcode")) {
			return System.identityHashCode(this);
		} else if (methodName.equals("toString")) {
			return super.toString();
		}
		return invokeNullableMethod(feignClientType, proxy, method, args);
	}

	protected Object invokeNullableMethod(Class<?> feignClientType, Object proxy, Method method, Object[] args) {
		return null;
	}

}
