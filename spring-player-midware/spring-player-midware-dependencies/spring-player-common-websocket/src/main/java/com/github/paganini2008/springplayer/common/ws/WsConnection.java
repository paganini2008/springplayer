package com.github.paganini2008.springplayer.common.ws;

/**
 * 
 * WsConnection
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface WsConnection {

	void connect();

	void close(boolean abandoned);

	boolean isClosed();

	boolean isAbandoned();

	void send(String text);

}