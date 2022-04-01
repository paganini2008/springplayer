package com.github.paganini2008.springplayer.common.messenger.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.messenger.model.DingTalkDTO;
import com.github.paganini2008.springplayer.common.messenger.model.EmailDTO;
import com.github.paganini2008.springplayer.common.messenger.model.MessageDTO;
import com.github.paganini2008.springplayer.common.messenger.model.PopupDTO;

/**
 * 
 * RemoteMessageService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@FeignClient(contextId = "remoteMessageService", name = "spring-player-messenger-service")
public interface RemoteMessageService {

	@PostMapping("/messager/send")
	ApiResult<String> sendMessage(@RequestBody MessageDTO message);

	@PostMapping("/messager/sendWithTemplate")
	ApiResult<String> sendMessage(@RequestBody MessageDTO message, @RequestParam("template") MultipartFile file);

	@PostMapping("/messager/dingtalk/send")
	ApiResult<String> sendDingTalkMessage(@RequestBody DingTalkDTO dingtalk);

	@PostMapping("/messager/dingtalk/sendWithTemplate")
	ApiResult<String> sendDingTalkMessage(@RequestBody DingTalkDTO dingtalk, @RequestParam("template") MultipartFile file);

	@PostMapping("/messager/email/send")
	ApiResult<String> sendEmail(@RequestBody EmailDTO email);

	@PostMapping("/messager/email/sendWithTemplate")
	ApiResult<String> sendEmail(@RequestBody EmailDTO email, @RequestParam("template") MultipartFile file);

	@PostMapping("/messager/popup/send")
	ApiResult<String> sendPopup(@RequestBody PopupDTO popup);

}
