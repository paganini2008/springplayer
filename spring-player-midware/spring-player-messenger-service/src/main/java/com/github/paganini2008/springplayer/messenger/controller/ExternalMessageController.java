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
import com.github.paganini2008.springplayer.common.messenger.model.EmailDTO;
import com.github.paganini2008.springplayer.common.messenger.model.MessageDTO;
import com.github.paganini2008.springplayer.common.messenger.model.MessageType;
import com.github.paganini2008.springplayer.messenger.ErrorCodes;
import com.github.paganini2008.springplayer.messenger.service.MessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * ExternalMessageController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Api(tags = "消息发送")
@Validated
@RestController
@RequestMapping("/messager")
public class ExternalMessageController {

	@Autowired
	private MessageService messagingService;

	@ApiOperation(value = "发送消息", notes = "发送消息")
	@PostMapping("/send")
	public ApiResult<String> sendMessage(@Validated @RequestBody MessageDTO message) {
		messagingService.sendMessage(message);
		return ApiResult.ok("发送成功");
	}

	@ApiOperation(value = "上传模板并发送消息", notes = "上传模板并发送消息")
	@PostMapping("/sendWithTemplate")
	public ApiResult<String> sendMessage(@Validated @ModelAttribute MessageDTO message,
			@RequestParam("template") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new BizException(ErrorCodes.TEMPLATE_UPLOAD_FAILED, HttpStatus.BAD_REQUEST);
		}
		String templateName = PathUtils.getBaseName(file.getOriginalFilename());
		String templateContent = IOUtils.toString(file.getInputStream(), "UTF-8");
		MessageType messageType = message.getType();
		switch (messageType) {
		case DING_TALK:
			message.getDingTalk().setTitle(templateName);
			message.getDingTalk().setTemplate(templateContent);
			break;
		case EMAIL:
			EmailDTO emailEntity = message.getEmail();
			if (StringUtils.isBlank(emailEntity.getSubject())) {
				emailEntity.setSubject(templateName);
			}
			emailEntity.setTemplate(templateContent);
			break;
		default:
			throw new UnsupportedOperationException();
		}
		messagingService.sendMessage(message);
		return ApiResult.ok("发送成功");
	}

}
