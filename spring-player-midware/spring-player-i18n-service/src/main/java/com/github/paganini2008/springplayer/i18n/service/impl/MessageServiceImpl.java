package com.github.paganini2008.springplayer.i18n.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paganini2008.springplayer.i18n.mapper.MessageMapper;
import com.github.paganini2008.springplayer.i18n.model.Message;
import com.github.paganini2008.springplayer.i18n.service.MessageService;

/**
 * 
 * MessageServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

	@Override
	public String getMessage(String project, String lang, String code) {
		LambdaQueryWrapper<Message> query = Wrappers.<Message>lambdaQuery().eq(Message::getProject, project).eq(Message::getLang, lang)
				.eq(Message::getCode, code);
		Message message = getOne(query);
		return message != null ? message.getText() : "";
	}

	@Override
	public List<Message> getMessages(String project, String lang) {
		LambdaQueryWrapper<Message> query = Wrappers.<Message>lambdaQuery().eq(Message::getProject, project).eq(Message::getLang, lang);
		return list(query);
	}

}
