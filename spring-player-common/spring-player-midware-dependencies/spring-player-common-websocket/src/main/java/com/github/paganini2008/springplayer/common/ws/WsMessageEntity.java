package com.github.paganini2008.springplayer.common.ws;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * WsMessageEntity
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
public class WsMessageEntity {

	private WsUser user;
	private String content;
	private Object attachment;
	private long timestamp;

	public WsMessageEntity(WsUser user, String content, Object attachment) {
		this.user = user;
		this.content = content;
		this.attachment = attachment;
		this.timestamp = System.currentTimeMillis();
	}

}
