package com.github.paganini2008.springplayer.channel.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.github.paganini2008.springplayer.channel.enums.ChannelType;

import io.micrometer.core.lang.Nullable;
import lombok.Data;

/**
 * 
 * ChannelDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Data
public class ChannelDTO {

	private Long id;
	
	@NotBlank(message = "渠道名称不能为空")
	private String name;

	@NotNull(message = "渠道类型不能为空")
	private ChannelType channelType;

	private @Nullable DingTalkDTO dingTalk;
	private @Nullable EmailDTO email;
	private @Nullable SmsDTO sms;
	private @Nullable WeChatDTO wechat;

	@Data
	public static class DingTalkDTO {

		private String type;
		
	}

	@Data
	public static class EmailDTO {

		private String subject;
		private String from;
		private String[] to;
		private String[] bcc;
		private String[] cc;

	}

	@Data
	public static class SmsDTO {

	}

	@Data
	public static class WeChatDTO {
	}

}
