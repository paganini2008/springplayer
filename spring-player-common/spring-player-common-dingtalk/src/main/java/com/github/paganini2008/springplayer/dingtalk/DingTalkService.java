package com.github.paganini2008.springplayer.dingtalk;

/**
 * 
 * DingTalkService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface DingTalkService {

	void sendTextMessage(DingTextMessage message) throws Exception;
	
}
