package com.github.paganini2008.springplayer.dingtalk;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.github.paganini2008.devtools.ArrayUtils;
import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.dingtalk.template.DingTalkTemplate;
import com.taobao.api.ApiException;

/**
 * 
 * DingTalkServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class DingTalkServiceImpl implements DingTalkService {

	@Autowired
	private DingTalkTemplate dingTalkTemplate;

	@Override
	public void sendTextMessage(DingTextMessage message) throws ApiException {
		DingTalkClient client = new DefaultDingTalkClient(message.getServiceUrl());
		OapiRobotSendRequest request = new OapiRobotSendRequest();
		request.setMsgtype("actionCard");
		OapiRobotSendRequest.Actioncard actioncard = new OapiRobotSendRequest.Actioncard();
		actioncard.setTitle(message.getTitle());
		String text = dingTalkTemplate.loadContent(message.getTitle(), message.getTemplate(), message.getVariables());
		String atMobiles = "";
		if (ArrayUtils.isNotEmpty(message.getMobiles())) {
			atMobiles = "@" + String.join(" @", Arrays.asList(message.getMobiles()));
		}
		if (StringUtils.isNotBlank(atMobiles)) {
			actioncard.setText(text + " " + atMobiles);
		} else {
			actioncard.setText(text);
		}
		actioncard.setBtnOrientation("1");
		request.setActionCard(actioncard);

		if (ArrayUtils.isNotEmpty(message.getMobiles())) {
			OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
			at.setAtMobiles(Arrays.asList(message.getMobiles()));
			request.setAt(at);
		}
		client.execute(request);
	}

}
