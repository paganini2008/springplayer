package com.github.paganini2008.springplayer.common.sentinel.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 
 * SentinelRuleUpdateEventListenerContainer
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public class SentinelRuleUpdateEventListenerContainer implements ApplicationListener<SentinelRuleUpdateEvent> {

	private final Map<String, SentinelRuleUpdateEventListener> listeners = new ConcurrentHashMap<>();

	public void subscribe(String name, SentinelRuleUpdateEventListener listener) {
		if (listener != null) {
			this.listeners.put(name, listener);
		}
	}

	public void unsubscribe(String name) {
		if (name != null) {
			this.listeners.remove(name);
		}
	}

	public int countOfListener() {
		return this.listeners.size();
	}

	@Override
	public synchronized void onApplicationEvent(SentinelRuleUpdateEvent event) {
		listeners.entrySet().forEach(e -> e.getValue().onApplicationEvent(event));
	}

}
