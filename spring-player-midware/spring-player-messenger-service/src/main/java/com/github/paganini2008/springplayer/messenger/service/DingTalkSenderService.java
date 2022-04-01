package com.github.paganini2008.springplayer.messenger.service;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

import org.springframework.stereotype.Component;

import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.beans.BeanUtils;
import com.github.paganini2008.springplayer.common.messenger.model.DingTalkDTO;
import com.github.paganini2008.springplayer.dingtalk.DingTalkService;
import com.github.paganini2008.springplayer.dingtalk.message.ActionCardMessage;
import com.github.paganini2008.springplayer.dingtalk.message.ActionCardMessage.BtnMessage;
import com.github.paganini2008.springplayer.dingtalk.message.FeedCardMessage;
import com.github.paganini2008.springplayer.dingtalk.message.FeedCardMessage.LinksMessage;
import com.github.paganini2008.springplayer.dingtalk.message.LinkMessage;
import com.github.paganini2008.springplayer.dingtalk.message.MarkdownMessage;
import com.github.paganini2008.springplayer.dingtalk.message.TextMessage;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

/**
 * 
 * DingTalkSenderService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
@AllArgsConstructor
public class DingTalkSenderService {

	private final DingTalkService dingTalkService;

	private final Semaphore lock = new Semaphore(8);

	@SneakyThrows
	public void processSendingDingTalk(DingTalkDTO entity) {
		lock.acquire();
		try {
			switch (entity.getType()) {
			case TEXT:
				TextMessage textMessage = convert2DingTextMessage(entity.getText());
				dingTalkService.sendTextMessage(textMessage);
				break;
			case LINK:
				LinkMessage linkMessage = convert2DingLinkMessage(entity.getLink());
				dingTalkService.sendLinkMessage(linkMessage);
				break;
			case MARKDOWN:
				MarkdownMessage markdownMessage = convert2DingMarkdownMessage(entity.getMarkdown());
				dingTalkService.sendMarkdownMessage(markdownMessage);
				break;
			case ACTION_CARD:
				ActionCardMessage actionCardMessage = convert2DingActionCardMessage(entity.getActionCard());
				dingTalkService.sendActionCardMessage(actionCardMessage);
				break;
			case FEED_CARD:
				FeedCardMessage feedCardMessage = convert2DingFeedCardMessage(entity.getFeedCard());
				dingTalkService.sendFeedCardMessage(feedCardMessage);
				break;
			}
		} finally {
			lock.release();
		}
	}

	private FeedCardMessage convert2DingFeedCardMessage(DingTalkDTO.FeedCard entity) {
		FeedCardMessage message = new FeedCardMessage();
		BeanUtils.copyProperties(entity, message);
		if (ArrayUtils.isNotEmpty(entity.getLinks())) {
			message.setLinks(
					Arrays.stream(entity.getLinks()).map(item -> new LinksMessage(item.getTitle(), item.getPicUrl(), item.getMessageUrl()))
							.toArray(l -> new LinksMessage[l]));
		}
		return message;
	}

	private ActionCardMessage convert2DingActionCardMessage(DingTalkDTO.ActionCard entity) {
		ActionCardMessage message = new ActionCardMessage();
		BeanUtils.copyProperties(entity, message);
		if (ArrayUtils.isNotEmpty(entity.getBtns())) {
			message.setBtns(Arrays.stream(entity.getBtns()).map(item -> new BtnMessage(item.getTitle(), item.getActionUrl()))
					.toArray(l -> new BtnMessage[l]));
		}
		return message;
	}

	private MarkdownMessage convert2DingMarkdownMessage(DingTalkDTO.Markdown entity) {
		MarkdownMessage message = new MarkdownMessage();
		BeanUtils.copyProperties(entity, message);
		return message;
	}

	private LinkMessage convert2DingLinkMessage(DingTalkDTO.Link entity) {
		LinkMessage message = new LinkMessage();
		BeanUtils.copyProperties(entity, message);
		return message;
	}

	private TextMessage convert2DingTextMessage(DingTalkDTO.Text entity) {
		TextMessage message = new TextMessage();
		BeanUtils.copyProperties(entity, message);
		return message;
	}

}
