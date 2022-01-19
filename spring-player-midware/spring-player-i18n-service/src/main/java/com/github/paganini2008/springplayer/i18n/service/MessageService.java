package com.github.paganini2008.springplayer.i18n.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paganini2008.springplayer.i18n.model.Message;

/**
 * 
 * MessageService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface MessageService extends IService<Message> {

	String getMessage(String group, String lang, String code);

	List<Message> getMessages(String group, String lang);

}
