package com.github.paganini2008.springplayer.common.redis.pubsub;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.event.EventListener;

import com.github.paganini2008.devtools.Observable;
import com.github.paganini2008.devtools.Observer;
import com.github.paganini2008.devtools.collection.CollectionUtils;
import com.github.paganini2008.devtools.reflection.MethodUtils;

/**
 * 
 * RedisMessageEventDispatcher
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class RedisMessageEventDispatcher implements BeanPostProcessor {

	private final Observable repeableOp = Observable.repeatable();
	private final Observable unrepeableOp = Observable.unrepeatable();

	@Override
	public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
		List<Method> methodList = MethodUtils.getMethodsWithAnnotation(bean.getClass(), RedisPubSub.class);
		if (CollectionUtils.isNotEmpty(methodList)) {
			for (final Method method : methodList) {
				RedisPubSub pubsub = method.getAnnotation(RedisPubSub.class);
				String channel = pubsub.value();
				Observer ob = (obs, args) -> {
					MethodUtils.invokeMethod(bean, method, (Object[]) args);
				};
				if (pubsub.repeatable()) {
					repeableOp.addObserver(channel, ob);
				} else {
					unrepeableOp.addObserver(channel, ob);
				}
			}

		}
		return bean;
	}

	@EventListener(RedisMessageEvent.class)
	public void handleRedisMessageEvent(RedisMessageEvent event) {
		Object[] args = new Object[] { event.getChannel(), event.getMessage() };
		repeableOp.notifyObservers(event.getChannel(), args);
		unrepeableOp.notifyObservers(event.getChannel(), args);
	}

}
