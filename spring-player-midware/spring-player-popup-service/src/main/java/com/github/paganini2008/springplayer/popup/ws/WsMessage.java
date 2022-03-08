package com.github.paganini2008.springplayer.popup.ws;

import com.github.paganini2008.springplayer.common.ws.WsUser;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * WsMessage
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@NoArgsConstructor
@Data
public class WsMessage<T> {

	private WsUser user;
	private T data;
	private long timestmap;

	public WsMessage(WsUser user, T data) {
		this.user = user;
		this.data = data;
		this.timestmap = System.currentTimeMillis();
	}

}
