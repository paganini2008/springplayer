package com.github.paganini2008.springplayer.common.dingtalk;

import com.github.paganini2008.springplayer.common.dingtalk.message.ActionCardMessage;
import com.github.paganini2008.springplayer.common.dingtalk.message.FeedCardMessage;
import com.github.paganini2008.springplayer.common.dingtalk.message.LinkMessage;
import com.github.paganini2008.springplayer.common.dingtalk.message.MarkdownMessage;
import com.github.paganini2008.springplayer.common.dingtalk.message.TextMessage;

/**
 * 
 * DingTalkService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface DingTalkService {

	boolean sendTextMessage(TextMessage message) throws Exception;
	
	boolean sendLinkMessage(LinkMessage message) throws Exception;
	
	boolean sendMarkdownMessage(MarkdownMessage message) throws Exception;
	
	boolean sendActionCardMessage(ActionCardMessage message) throws Exception;
	
	boolean sendFeedCardMessage(FeedCardMessage message) throws Exception;
	
}
