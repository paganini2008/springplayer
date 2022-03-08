package com.github.paganini2008.springplayer.common.ws;

import static com.github.paganini2008.springplayer.common.ws.WsContants.DEFAULT_BOARDCAST_NAME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.github.paganini2008.devtools.collection.MapUtils;
import com.github.paganini2008.springplayer.redis.pubsub.RedisPubSub;

/**
 * 
 * WsHandlerContext
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class WsHandlerContext {

	public static final String CONTEXT_ID = UUID.randomUUID().toString();
	private final Map<String, List<WsHandler>> keyHandlers = new ConcurrentHashMap<>();

	public void addHandler(String topic, WsHandler handler) {
		List<WsHandler> list = MapUtils.get(keyHandlers, topic, () -> new CopyOnWriteArrayList<>());
		list.add(handler);
	}

	public void removeHandler(String topic, WsHandler handler) {
		List<WsHandler> handlers = keyHandlers.get(topic);
		if (handlers != null) {
			handlers.remove(handler);
		}
		if (handlers.isEmpty()) {
			keyHandlers.remove(topic);
		}
	}

	public List<WsHandler> getHandlers(Object key) {
		if (!keyHandlers.containsKey(key)) {
			return Collections.emptyList();
		}
		return new ArrayList<WsHandler>(keyHandlers.get(key));
	}

	public int countOfHandlers(Object key) {
		return keyHandlers.containsKey(key) ? keyHandlers.get(key).size() : 0;
	}

	public int countOfHandlers() {
		return keyHandlers.size();
	}

	@RedisPubSub(DEFAULT_BOARDCAST_NAME)
	public final void handleBoardcast(String channel, Object data) {
		WsMessageEntity messageEntity = (WsMessageEntity) data;
		getHandlers(messageEntity.getUser().getTopic()).forEach(handler -> {
			handler.receiveMessage(messageEntity.getUser(), messageEntity.getContent(), messageEntity.getAttachment(),
					messageEntity.getTimestamp());
		});
	}

}
