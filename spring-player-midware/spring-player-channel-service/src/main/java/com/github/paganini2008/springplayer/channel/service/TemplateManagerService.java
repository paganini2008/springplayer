package com.github.paganini2008.springplayer.channel.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.paganini2008.springplayer.channel.model.Template;
import com.github.paganini2008.springplayer.channel.pojo.TemplateDTO;
import com.github.paganini2008.springplayer.common.id.IdGenerator;

/**
 * 
 * TemplateManagerService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Service
public class TemplateManagerService {

	@Autowired
	private IdGenerator idGenerator;

	@Autowired
	private TemplateService templateService;

	public long saveTemplate(TemplateDTO dto) {
		Template template = null;
		if (dto.getId() != null) {
			template = getById(dto.getId());
		}
		if (template == null) {
			template = getByName(dto.getName());
		}
		if (template == null) {
			template = new Template();
			template.setId(idGenerator.generateId());
			template.setCreateTime(LocalDateTime.now());
		}
		template.setName(dto.getName());
		template.setContent(dto.getContent());
		template.setFormat(dto.getFormat());
		template.setUpdateTime(LocalDateTime.now());
		templateService.saveOrUpdate(template);
		return template.getId();
	}

	public Template getById(long templateId) {
		return templateService.getById(templateId);
	}

	public Template getByName(String name) {
		LambdaQueryWrapper<Template> query = Wrappers.<Template>lambdaQuery();
		query = query.eq(Template::getName, name);
		return templateService.getOne(query);
	}

	public boolean deleteById(long templateId) {
		return templateService.removeById(templateId);
	}

}
