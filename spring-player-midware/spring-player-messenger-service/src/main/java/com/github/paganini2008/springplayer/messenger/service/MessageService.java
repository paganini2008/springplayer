package com.github.paganini2008.springplayer.messenger.service;

/**
 * 
 * MessageService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface MessageService {

	void sendMessage(Object message);

	void handleMessage(Object message);

}
