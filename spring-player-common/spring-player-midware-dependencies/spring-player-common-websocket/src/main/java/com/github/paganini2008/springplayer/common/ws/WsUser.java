package com.github.paganini2008.springplayer.common.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * WsUser
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WsUser {

	private String topic;
	private String userId;
	private String sessionId;

}
