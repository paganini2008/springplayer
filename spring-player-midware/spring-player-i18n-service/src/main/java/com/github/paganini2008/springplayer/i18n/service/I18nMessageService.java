package com.github.paganini2008.springplayer.i18n.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paganini2008.springplayer.i18n.model.I18nMessage;
import com.github.paganini2008.springplayer.i18n.vo.I18nMessageVO;

/**
 * 
 * I18nMessageService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface I18nMessageService extends IService<I18nMessage> {

	String getMessage(String group, String lang, String code);

	List<I18nMessageVO> getMessages(String group, String lang);

}
