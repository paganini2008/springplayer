package com.github.paganini2008.springplayer.messenger.service;

/**
 * 
 * MessagingService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface MessagingService {

	void sendMessage(Object message);

	void handleMessage(Object message);

}
