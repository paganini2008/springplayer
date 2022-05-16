package com.github.paganini2008.springplayer.common.ws;

import org.java_websocket.handshake.ServerHandshake;

/**
 * 
 * WsStateListener
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface WsStateListener {

	default void onOpen(ServerHandshake handshakedata) {
	}

	default void onClose(int code, String reason, boolean remote) {
	}

	default void onError(Exception e) {
	}

}
