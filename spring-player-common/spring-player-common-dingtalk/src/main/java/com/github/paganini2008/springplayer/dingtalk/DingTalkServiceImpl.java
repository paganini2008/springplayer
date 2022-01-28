package com.github.paganini2008.springplayer.dingtalk;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.request.OapiRobotSendRequest.Btns;
import com.dingtalk.api.request.OapiRobotSendRequest.Links;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.collection.LruMap;
import com.github.paganini2008.devtools.collection.MapUtils;
import com.github.paganini2008.springplayer.dingtalk.message.ActionCardMessage;
import com.github.paganini2008.springplayer.dingtalk.message.DingMessage;
import com.github.paganini2008.springplayer.dingtalk.message.FeedCardMessage;
import com.github.paganini2008.springplayer.dingtalk.message.LinkMessage;
import com.github.paganini2008.springplayer.dingtalk.message.MarkdownMessage;
import com.github.paganini2008.springplayer.dingtalk.message.TextMessage;
import com.github.paganini2008.springplayer.dingtalk.template.DingTalkTemplate;
import com.taobao.api.ApiException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * DingTalkServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class DingTalkServiceImpl implements DingTalkService {

	@Autowired
	private DingTalkTemplate dingTalkTemplate;

	private final LruMap<String, DingTalkClient> clientHolder = new LruMap<>();

	@Override
	public boolean sendTextMessage(TextMessage message) throws Exception {
		DingTalkClient client = MapUtils.get(clientHolder, message.getServiceUrl(), () -> {
			return new DefaultDingTalkClient(message.getServiceUrl());
		});
		OapiRobotSendRequest request = new OapiRobotSendRequest();
		request.setMsgtype("text");
		OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
		String content = getContent(message);
		text.setContent(content);

		request.setText(text);

		if (ArrayUtils.isNotEmpty(message.getMobiles())) {
			OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
			at.setAtMobiles(Arrays.asList(message.getMobiles()));
			at.setIsAtAll(true);
			request.setAt(at);
		}
		OapiRobotSendResponse response = client.execute(request);
		log.info("Success: {}, Body: {}", response.isSuccess(), response.getBody());
		return response.isSuccess();
	}

	@Override
	public boolean sendLinkMessage(LinkMessage message) throws Exception {
		DingTalkClient client = MapUtils.get(clientHolder, message.getServiceUrl(), () -> {
			return new DefaultDingTalkClient(message.getServiceUrl());
		});
		OapiRobotSendRequest request = new OapiRobotSendRequest();
		request.setMsgtype("link");

		OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
		link.setMessageUrl(message.getMessageUrl());
		link.setPicUrl(message.getPicUrl());
		link.setTitle(message.getTitle());
		String content = getContent(message);
		link.setText(content);

		request.setLink(link);

		if (ArrayUtils.isNotEmpty(message.getMobiles())) {
			OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
			at.setIsAtAll(true);
			at.setAtMobiles(Arrays.asList(message.getMobiles()));
			request.setAt(at);
		}

		OapiRobotSendResponse response = client.execute(request);
		log.info("Success: {}, Body: {}", response.isSuccess(), response.getBody());
		return response.isSuccess();
	}

	@Override
	public boolean sendMarkdownMessage(MarkdownMessage message) throws Exception {
		DingTalkClient client = MapUtils.get(clientHolder, message.getServiceUrl(), () -> {
			return new DefaultDingTalkClient(message.getServiceUrl());
		});
		OapiRobotSendRequest request = new OapiRobotSendRequest();
		request.setMsgtype("markdown");

		OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
		markdown.setTitle(message.getTitle());
		String content = getContent(message);
		markdown.setText(content);

		request.setMarkdown(markdown);

		if (ArrayUtils.isNotEmpty(message.getMobiles())) {
			OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
			at.setIsAtAll(true);
			at.setAtMobiles(Arrays.asList(message.getMobiles()));
			request.setAt(at);
		}
		OapiRobotSendResponse response = client.execute(request);
		log.info("Success: {}, Body: {}", response.isSuccess(), response.getBody());
		return response.isSuccess();
	}

	@Override
	public boolean sendFeedCardMessage(FeedCardMessage message) throws Exception {
		DingTalkClient client = MapUtils.get(clientHolder, message.getServiceUrl(), () -> {
			return new DefaultDingTalkClient(message.getServiceUrl());
		});
		OapiRobotSendRequest request = new OapiRobotSendRequest();
		request.setMsgtype("feedCard");

		OapiRobotSendRequest.Feedcard feedCard = new OapiRobotSendRequest.Feedcard();

		feedCard.setLinks(Arrays.stream(message.getLinks()).map(item -> {
			Links links = new Links();
			links.setTitle(item.getTitle());
			links.setMessageURL(item.getMessageUrl());
			links.setPicURL(item.getPicUrl());
			return links;
		}).collect(Collectors.toList()));
		request.setFeedCard(feedCard);
		
		if (ArrayUtils.isNotEmpty(message.getMobiles())) {
			OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
			at.setIsAtAll(true);
			at.setAtMobiles(Arrays.asList(message.getMobiles()));
			request.setAt(at);
		}

		OapiRobotSendResponse response = client.execute(request);
		log.info("Success: {}, Body: {}", response.isSuccess(), response.getBody());
		return response.isSuccess();
	}

	@Override
	public boolean sendActionCardMessage(ActionCardMessage message) throws ApiException {

		DingTalkClient client = new DefaultDingTalkClient(message.getServiceUrl());
		OapiRobotSendRequest request = new OapiRobotSendRequest();
		request.setMsgtype("actionCard");
		OapiRobotSendRequest.Actioncard actionCard = new OapiRobotSendRequest.Actioncard();
		actionCard.setTitle(message.getTitle());

		if (StringUtils.isNotBlank(message.getSingleTitle())) {
			actionCard.setSingleTitle(message.getSingleTitle());
		}
		if (StringUtils.isNotBlank(message.getSingleUrl())) {
			actionCard.setSingleURL(message.getSingleUrl());
		}
		String content = getContent(message);
		actionCard.setText(content);

		if (StringUtils.isNotBlank(message.getBtnOrientation())) {
			actionCard.setBtnOrientation(message.getBtnOrientation());
		}
		if (StringUtils.isNotBlank(message.getHideAvatar())) {
			actionCard.setHideAvatar(message.getHideAvatar());
		}
		if (ArrayUtils.isNotEmpty(message.getBtns())) {
			actionCard.setBtns(Arrays.stream(message.getBtns()).map(item -> {
				Btns btns = new Btns();
				btns.setActionURL(item.getActionUrl());
				btns.setTitle(item.getTitle());
				return btns;
			}).collect(Collectors.toList()));
		}
		request.setActionCard(actionCard);

		if (ArrayUtils.isNotEmpty(message.getMobiles())) {
			OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
			at.setIsAtAll(true);
			at.setAtMobiles(Arrays.asList(message.getMobiles()));
			request.setAt(at);
		}
		OapiRobotSendResponse response = client.execute(request);
		log.info("Success: {}, Body: {}", response.isSuccess(), response.getBody());
		return response.isSuccess();
	}

	private String getContent(DingMessage message) {
		String content = dingTalkTemplate.loadContent("", message.getTemplate(), message.getVariables());
		String atMobiles = "";
		if (ArrayUtils.isNotEmpty(message.getMobiles())) {
			atMobiles = "@" + String.join(" @", Arrays.asList(message.getMobiles()));
		}
		if (StringUtils.isNotBlank(atMobiles)) {
			content = content + " " + atMobiles;
		}
		return content;
	}

}
