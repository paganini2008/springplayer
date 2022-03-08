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
	
	void send(String content);
	
	void disconnect();
	
	void close();
	
	boolean isActive();
	
}
