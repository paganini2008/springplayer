package com.github.paganini2008.springplayer.popup.ws;

import java.util.UUID;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.JacksonUtils;
import com.github.paganini2008.springplayer.common.ws.WsHandler;
import com.github.paganini2008.springplayer.common.ws.WsUser;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * TopicWsHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@ServerEndpoint(value = "/ws/{topic}")
@Component
public class TopicWsHandler extends WsHandler {

	Session session;
	String sessionId;
	WsUser wsUser;

	@OnOpen
	public void onOpen(Session session, @PathParam("topic") String topic, EndpointConfig config) {
		this.session = session;
		this.sessionId = StringUtils.isNotBlank(session.getId()) ? session.getId() : UUID.randomUUID().toString();
		this.wsUser = new WsUser(topic, null, sessionId);
		log.info("Open new session: {}", wsUser);
	}

	@Override
	public WsUser currentUser() {
		return wsUser;
	}

	@OnMessage
	public void onMessage(String content) {
		if (log.isDebugEnabled()) {
			log.debug(content);
		}
		boardcast(wsUser, content, null);
	}

	@OnClose
	public void onClose(@PathParam("topic") String topic, CloseReason reason) {
		log.info("Close session: {}, reason: {}", wsUser, reason != null ? reason.getReasonPhrase() : "<None>");
	}

	@Override
	protected void receiveMessage(WsUser fromUser, String content, Object attachment, long timestamp) {
		WsMessage<String> wsMessage = new WsMessage<String>(wsUser, content);
		try {
			session.getAsyncRemote().sendText(JacksonUtils.toJsonString(wsMessage));
			if (log.isDebugEnabled()) {
				log.debug("Receive remote message from: " + fromUser);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
