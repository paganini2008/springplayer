package com.github.paganini2008.springplayer.common.dingtalk.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * FeedCardMessage
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class FeedCardMessage extends DingMessage {
	
	private LinksMessage[] links;

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LinksMessage {
		private String title;
		private String picUrl;
		private String messageUrl;
	}

}
