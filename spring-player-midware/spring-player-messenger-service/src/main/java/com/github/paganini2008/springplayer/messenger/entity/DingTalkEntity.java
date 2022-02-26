package com.github.paganini2008.springplayer.messenger.entity;

import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.messenger.DingMessageType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("钉钉请求实体")
@Getter
@Setter
public class DingTalkEntity {

	@ApiModelProperty("钉钉消息类型")
	@NotNull(message = "钉钉消息类型不能为空")
	private Integer type;

	@ApiModelProperty("文本消息")
	private @Nullable Text text;

	@ApiModelProperty("链接消息")
	private @Nullable Link link;

	@ApiModelProperty("Markdown消息")
	private @Nullable Markdown markdown;

	@ApiModelProperty("ActionCard消息")
	private @Nullable ActionCard actionCard;

	@ApiModelProperty("FeedCard消息")
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

	@ApiModel("钉钉消息")
	@Getter
	@Setter
	@ToString
	public abstract static class DingMessageModel {

		@ApiModelProperty("钉钉服务地址")
		@NotBlank(message = "钉钉服务地址不能为空")
		private String serviceUrl;

		@ApiModelProperty("消息标题")
		private String title;

		@ApiModelProperty("消息模板")
		private String template;

		@ApiModelProperty("模板变量")
		private Map<String, Object> variables;

		@ApiModelProperty("手机")
		private String[] mobiles;

	}

	@ApiModel("文本消息")
	@ToString(callSuper = true)
	public static class Text extends DingMessageModel {
	}

	@ApiModel("链接消息")
	@Getter
	@Setter
	@ToString(callSuper = true)
	public static class Link extends DingMessageModel {

		@ApiModelProperty("图片地址")
		@NotBlank(message = "图片地址不能为空")
		private String picUrl;

		@ApiModelProperty("跳转链接")
		@NotBlank(message = "消息地址不能为空")
		private String messageUrl;
	}

	@ApiModel("Markdown消息")
	@Getter
	@Setter
	@ToString(callSuper = true)
	public static class Markdown extends DingMessageModel {
	}

	@ApiModel("ActionCard消息")
	@Getter
	@Setter
	@ToString(callSuper = true)
	public static class ActionCard extends DingMessageModel {

		@ApiModelProperty("标题")
		private String singleTitle;

		@ApiModelProperty("跳转链接")
		private String singleUrl;

		@ApiModelProperty("0-按钮竖直排列,1-按钮横向排列")
		private String btnOrientation;

		@ApiModelProperty("是否隐藏头像")
		private String hideAvatar;

		@ApiModelProperty("按钮组件")
		private Btn[] btns;
	}

	@ApiModel("FeedCard消息")
	@Getter
	@Setter
	@ToString(callSuper = true)
	public static class FeedCard extends DingMessageModel {

		@ApiModelProperty("链接组件")
		@NotEmpty(message = "消息链接不能为空")
		private Links[] links;
	}

	@ApiModel("按钮组件")
	@Getter
	@Setter
	public static class Btn {

		@ApiModelProperty("标题")
		private String title;

		@ApiModelProperty("跳转链接")
		private String actionUrl;

	}

	@ApiModel("链接组件")
	@Getter
	@Setter
	public static class Links {

		@ApiModelProperty("标题")
		private String title;

		@ApiModelProperty("图片链接")
		private String picUrl;

		@ApiModelProperty("跳转链接")
		private String messageUrl;
	}

}
