package com.github.paganini2008.springplayer.messenger.entity;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.github.paganini2008.springplayer.messenger.MessageType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("消息请求实体")
public class MessagingEntity {

	@ApiModelProperty("消息类型")
	@NotNull(message = "消息类型不能为空")
	private Integer type;
	
	@ApiModelProperty("钉钉请求实体")
	private @Nullable DingTalkEntity dingTalk;
	
	@ApiModelProperty("邮件请求实体")
	private @Nullable EmailEntity email;
	
	public MessageType getType() {
		return MessageType.valueOf(type);
	}

}
