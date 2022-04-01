package com.github.paganini2008.springplayer.common.ws;

import static com.github.paganini2008.springplayer.common.ws.WsContants.DEFAULT_BOARDCAST_NAME;

import com.github.paganini2008.springplayer.common.ApplicationContextUtils;
import com.github.paganini2008.springplayer.common.redis.pubsub.RedisPubSubService;

/**
 * 
 * WsHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public abstract class WsHandler {

	public void boardcast(WsUser user, String content, Object attachment) {
		RedisPubSubService pubSubService = ApplicationContextUtils.getBean(RedisPubSubService.class);
		WsMessageEntity messageEntity = new WsMessageEntity(user, content, attachment);
		pubSubService.convertAndMulticast(DEFAULT_BOARDCAST_NAME, messageEntity);
	}

	public abstract WsUser currentUser();

	protected abstract void receiveMessage(WsUser user, String content, Object attachment, long timestamp);

}
