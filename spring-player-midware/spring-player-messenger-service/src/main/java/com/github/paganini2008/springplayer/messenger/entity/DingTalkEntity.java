package com.github.paganini2008.springplayer.messenger.entity;

import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.messenger.DingMessageType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * DingTalkEntity
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Getter
@Setter
public class DingTalkEntity {

	@NotNull(message = "钉钉消息类型不能为空")
	private Integer type;

	private @Nullable Text text;

	private @Nullable Link link;

	private @Nullable Markdown markdown;

	private @Nullable ActionCard actionCard;

	private @Nullable FeedCard feedCard;

	public void setDefaultTitle(String title) {
		if (type == null) {
			return;
		}
		DingMessageModel parent = null;
		switch (getType()) {
		case TEXT:
			parent = text;
			break;
		case LINK:
			parent = link;
			break;
		case MARKDOWN:
			parent = markdown;
			break;
		case ACTION_CARD:
			parent = actionCard;
			break;
		case FEED_CARD:
			parent = feedCard;
			break;
		}
		if (parent != null && StringUtils.isBlank(parent.getTitle())) {
			parent.setTitle(title);
		}
	}

	public void setDefaultTemplate(String content) {
		if (type == null) {
			return;
		}
		DingMessageModel parent = null;
		switch (getType()) {
		case TEXT:
			parent = text;
			break;
		case LINK:
			parent = link;
			break;
		case MARKDOWN:
			parent = markdown;
			break;
		case ACTION_CARD:
			parent = actionCard;
			break;
		case FEED_CARD:
			parent = feedCard;
			break;
		}
		if (parent != null && StringUtils.isBlank(parent.getTemplate())) {
			parent.setTemplate(content);
		}
	}
	
	public DingMessageType getType() {
		return DingMessageType.valueOf(type);
	}
	
	@Getter
	@Setter
	@ToString
	public abstract static class DingMessageModel {

		@NotBlank(message = "钉钉服务地址不能为空")
		private String serviceUrl;

		private String title;
		private String template;

		private Map<String, Object> variables;
		private String[] mobiles;

	}

	@ToString(callSuper = true)
	public static class Text extends DingMessageModel {
	}

	@Getter
	@Setter
	@ToString(callSuper = true)
	public static class Link extends DingMessageModel {

		@NotBlank(message = "图片地址不能为空")
		private String picUrl;

		@NotBlank(message = "消息地址不能为空")
		private String messageUrl;
	}

	@Getter
	@Setter
	@ToString(callSuper = true)
	public static class Markdown extends DingMessageModel {
	}

	@Getter
	@Setter
	@ToString(callSuper = true)
	public static class ActionCard extends DingMessageModel {

		private String singleTitle;
		private String singleUrl;
		private String btnOrientation;
		private String hideAvatar;

		private Btn[] btns;
	}

	@Getter
	@Setter
	@ToString(callSuper = true)
	public static class FeedCard extends DingMessageModel {

		@NotEmpty(message = "消息链接不能为空")
		private Links[] links;
	}

	@Getter
	@Setter
	public static class Btn {

		private String title;
		private String actionUrl;

	}

	@Getter
	@Setter
	public static class Links {

		private String title;
		private String picUrl;
		private String messageUrl;
	}
	
}
