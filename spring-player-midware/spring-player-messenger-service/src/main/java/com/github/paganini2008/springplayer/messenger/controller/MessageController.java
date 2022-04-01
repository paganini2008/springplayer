package com.github.paganini2008.springplayer.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.io.IOUtils;
import com.github.paganini2008.devtools.io.PathUtils;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.BizException;
import com.github.paganini2008.springplayer.common.messenger.model.DingTalkDTO;
import com.github.paganini2008.springplayer.common.messenger.model.EmailDTO;
import com.github.paganini2008.springplayer.common.messenger.model.MessageDTO;
import com.github.paganini2008.springplayer.common.messenger.model.PopupDTO;
import com.github.paganini2008.springplayer.messenger.ErrorCodes;
import com.github.paganini2008.springplayer.messenger.service.MessageService;

import io.swagger.annotations.Api;

/**
 * 
 * MessageController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Api(tags = "消息发送")
@Validated
@RestController
@RequestMapping("/messager")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@PostMapping("/dingtalk/send")
	public ApiResult<String> sendDingTalkMessage(@Validated @RequestBody DingTalkDTO dingTalk) {
		messageService.sendMessage(MessageDTO.forDingTalk(dingTalk));
		return ApiResult.ok("发送成功");
	}

	@PostMapping("/email/send")
	public ApiResult<String> sendEmail(@Validated @RequestBody EmailDTO email) {
		messageService.sendMessage(MessageDTO.forEmail(email));
		return ApiResult.ok("发送成功");
	}

	@PostMapping("/popup/send")
	public ApiResult<String> sendPopup(@Validated @RequestBody PopupDTO popup) {
		messageService.sendMessage(MessageDTO.forPopup(popup));
		return ApiResult.ok("发送成功");
	}

	@PostMapping("/dingtalk/sendWithTemplate")
	public ApiResult<String> sendDingTalkMessage(@Validated @ModelAttribute DingTalkDTO dingTalk,
			@RequestParam("template") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new BizException(ErrorCodes.TEMPLATE_UPLOAD_FAILED, HttpStatus.BAD_REQUEST);
		}
		String templateName = PathUtils.getBaseName(file.getOriginalFilename());
		String templateContent = IOUtils.toString(file.getInputStream(), "UTF-8");
		dingTalk.setTitle(templateName);
		dingTalk.setTemplate(templateContent);
		messageService.sendMessage(MessageDTO.forDingTalk(dingTalk));
		return ApiResult.ok("发送成功");
	}

	@PostMapping("/email/sendWithTemplate")
	public ApiResult<String> sendEmail(@Validated @ModelAttribute EmailDTO email, @RequestParam("template") MultipartFile file)
			throws Exception {
		if (file.isEmpty()) {
			throw new BizException(ErrorCodes.TEMPLATE_UPLOAD_FAILED, HttpStatus.BAD_REQUEST);
		}
		String templateName = PathUtils.getBaseName(file.getOriginalFilename());
		String templateContent = IOUtils.toString(file.getInputStream(), "UTF-8");
		if (StringUtils.isBlank(email.getSubject())) {
			email.setSubject(templateName);
		}
		email.setTemplate(templateContent);
		messageService.sendMessage(MessageDTO.forEmail(email));
		return ApiResult.ok("发送成功");
	}

}
