package com.github.paganini2008.springplayer.messenger.service;

import org.springframework.stereotype.Component;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.messenger.model.PopupDTO;
import com.github.paganini2008.springplayer.common.ws.WsConnection;
import com.github.paganini2008.springplayer.common.ws.WsConnectionPool;

import lombok.AllArgsConstructor;

/**
 * 
 * PopupSenderService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
@AllArgsConstructor
public class PopupSenderService {

	private final WsConnectionPool connectionPool;

	public void processSendingPopup(PopupDTO popup) {
		WsConnection connection = connectionPool.get(popup.getWsUrl());
		String text = StringUtils.parseText(popup.getTemplate(), "{", "}", popup.getVariables());
		connection.send(text);
	}

}
