package com.github.paganini2008.springplayer.common.dingtalk.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * LinkMessage
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class LinkMessage extends DingMessage {
	
	private String title;
	private String picUrl;
	private String messageUrl;

}
