package com.github.paganini2008.springplayer.common.ws;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * WsClientHolder
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class WsClientHolder extends WebSocketClient {

	WsClientHolder(URI serverUri, WsStateListener stateListener) {
		super(serverUri);
		this.stateListener = stateListener;
	}

	private final WsStateListener stateListener;

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		if (log.isInfoEnabled()) {
			log.info("[OnOpen] status: {}, message: {}", handshakedata.getHttpStatus(), handshakedata.getHttpStatusMessage());
		}
		stateListener.onOpen(handshakedata);
	}

	@Override
	public void onMessage(String message) {
		if (log.isInfoEnabled()) {
			log.info("[OnMessage] content: {}", message);
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		if (log.isInfoEnabled()) {
			log.info("[OnClose] code: {}, reason: {}, remote: {}", code, reason, remote);
		}
		stateListener.onClose(code, reason, remote);
	}

	@Override
	public void onError(Exception e) {
		if (log.isErrorEnabled()) {
			log.error(e.getMessage(), e);
		}
		stateListener.onError(e);
	}

}
