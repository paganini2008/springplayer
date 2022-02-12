package com.github.paganini2008.springplayer.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.github.paganini2008.springplayer.messenger.ErrorCodes;
import com.github.paganini2008.springplayer.messenger.MessageType;
import com.github.paganini2008.springplayer.messenger.entity.EmailEntity;
import com.github.paganini2008.springplayer.messenger.entity.MessagingEntity;
import com.github.paganini2008.springplayer.messenger.service.MessagingService;

/**
 * 
 * MessagingController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Validated
@RestController
@RequestMapping("/messaging")
public class MessagingController {

	@Autowired
	private MessagingService messagingService;

	@GetMapping("/echo")
	public ApiResult<String> echo(@RequestParam("q") String q) {
		return ApiResult.ok(q);
	}

	@PostMapping("/send")
	public ApiResult<String> sendMessage(@Validated @RequestBody MessagingEntity messagingEntity) {
		messagingService.sendMessage(messagingEntity);
		return ApiResult.ok("发送成功");
	}

	@PostMapping("/send2")
	public ApiResult<String> sendMessage(@Validated @ModelAttribute MessagingEntity messagingEntity,
			@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new BizException(ErrorCodes.TEMPLATE_UPLOAD_FAILED, HttpStatus.BAD_REQUEST);
		}
		String templateName = PathUtils.getBaseName(file.getOriginalFilename());
		String templateContent = IOUtils.toString(file.getInputStream(), "UTF-8");
		MessageType messageType = messagingEntity.getType();
		switch (messageType) {
		case DING_TALK:
			messagingEntity.getDingTalk().setDefaultTitle(templateName);
			messagingEntity.getDingTalk().setDefaultTemplate(templateContent);
			break;
		case EMAIL:
			EmailEntity emailEntity = messagingEntity.getEmail();
			if (StringUtils.isBlank(emailEntity.getSubject())) {
				emailEntity.setSubject(templateName);
			}
			emailEntity.setTemplate(templateContent);
			break;
		default:
			throw new UnsupportedOperationException();
		}

		messagingService.sendMessage(messagingEntity);
		return ApiResult.ok("发送成功");
	}

}
