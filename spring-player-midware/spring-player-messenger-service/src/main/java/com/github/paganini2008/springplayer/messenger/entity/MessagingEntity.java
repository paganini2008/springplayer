package com.github.paganini2008.springplayer.messenger.entity;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.github.paganini2008.springplayer.messenger.MessageType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * MessagingEntity
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class MessagingEntity {

	@NotNull(message = "消息类型不能为空")
	private Integer type;
	private @Nullable DingTalkEntity dingTalk;
	private @Nullable EmailEntity email;
	
	public MessageType getType() {
		return MessageType.valueOf(type);
	}

}
