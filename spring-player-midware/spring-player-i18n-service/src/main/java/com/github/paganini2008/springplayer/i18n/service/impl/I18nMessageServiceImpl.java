package com.github.paganini2008.springplayer.i18n.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paganini2008.devtools.beans.BeanUtils;
import com.github.paganini2008.springplayer.i18n.mapper.I18nMessageMapper;
import com.github.paganini2008.springplayer.i18n.model.I18nMessage;
import com.github.paganini2008.springplayer.i18n.service.I18nMessageService;
import com.github.paganini2008.springplayer.i18n.vo.I18nMessageVO;

/**
 * 
 * I18nMessageServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class I18nMessageServiceImpl extends ServiceImpl<I18nMessageMapper, I18nMessage> implements I18nMessageService {

	@Override
	public String getMessage(String project, String lang, String code) {
		LambdaQueryWrapper<I18nMessage> query = Wrappers.<I18nMessage>lambdaQuery().eq(I18nMessage::getProject, project)
				.eq(I18nMessage::getLang, lang).eq(I18nMessage::getCode, code);
		I18nMessage message = getOne(query);
		return message != null ? message.getText() : "";
	}

	@Override
	public List<I18nMessageVO> getMessages(String project, String lang) {
		LambdaQueryWrapper<I18nMessage> query = Wrappers.<I18nMessage>lambdaQuery().eq(I18nMessage::getProject, project)
				.eq(I18nMessage::getLang, lang);
		return list(query).stream().map(e -> {
			I18nMessageVO vo = new I18nMessageVO();
			BeanUtils.copyProperties(e, vo);
			return vo;
		}).collect(Collectors.toList());
	}

}
