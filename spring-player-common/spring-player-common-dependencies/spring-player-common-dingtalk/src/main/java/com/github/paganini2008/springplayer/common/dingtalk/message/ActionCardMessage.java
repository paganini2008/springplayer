package com.github.paganini2008.springplayer.common.dingtalk.message;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * ActionCardMessage
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ActionCardMessage extends DingMessage {

	private String title;
	private String singleTitle;
	private String singleUrl;
	private String btnOrientation = "1";
	private String hideAvatar = "0";
	
	private @Nullable BtnMessage[] btns;

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BtnMessage {

		private String title;
		private String actionUrl;

	}

}
