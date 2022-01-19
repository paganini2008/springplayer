package com.github.paganini2008.springplayer.i18n.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.i18n.model.Message;
import com.github.paganini2008.springplayer.i18n.service.MessageService;

/**
 * 
 * MessageController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RestController
@RequestMapping("/i18n")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@GetMapping("/message/{project}/{lang}/{code}")
	public ApiResult<String> getMessage(@PathVariable("project") String project, @PathVariable("lang") String lang,
			@PathVariable("code") String code) {
		return ApiResult.ok(messageService.getMessage(project, lang, code));
	}

	@GetMapping("/message/{project}/{lang}")
	public ApiResult<List<Message>> getMessages(@PathVariable("project") String project, @PathVariable("lang") String lang) {
		return ApiResult.ok(messageService.getMessages(project, lang));
	}

}
