package com.github.paganini2008.springplayer.common.messenger.model;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * MessageDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
@ApiModel("消息请求实体")
public class MessageDTO {

	@ApiModelProperty("消息类型")
	@NotNull(message = "消息类型不能为空")
	private MessageType type;

	@ApiModelProperty("钉钉请求实体")
	private @Nullable DingTalkDTO dingTalk;

	@ApiModelProperty("邮件请求实体")
	private @Nullable EmailDTO email;

	@ApiModelProperty("弹窗实体")
	private @Nullable PopupDTO popup;

	public MessageDTO() {
	}

	public static MessageDTO forDingTalk(DingTalkDTO dingTalk) {
		MessageDTO message = new MessageDTO();
		message.setDingTalk(dingTalk);
		message.setType(MessageType.DING_TALK);
		return message;
	}

	public static MessageDTO forEmail(EmailDTO email) {
		MessageDTO message = new MessageDTO();
		message.setEmail(email);
		message.setType(MessageType.EMAIL);
		return message;
	}
	
	public static MessageDTO forPopup(PopupDTO popup) {
		MessageDTO message = new MessageDTO();
		message.setPopup(popup);
		message.setType(MessageType.POPUP);
		return message;
	}

}
