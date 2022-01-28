package com.github.paganini2008.springplayer.i18n.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.i18n.vo.I18nMessageVO;

/**
 * 
 * RemoteI18nService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@FeignClient(contextId = "remoteI18nService", value = "spring-player-i18n-service")
public interface RemoteI18nService {

	@GetMapping("/i18n/message/{project}/{lang}/{code}")
	ApiResult<String> getMessage(@PathVariable("project") String project, @PathVariable("lang") String lang, @PathVariable("code") String code);
	
	@GetMapping("/i18n/message/{project}/{lang}")
	ApiResult<List<I18nMessageVO>> getMessages(@PathVariable("project") String project, @PathVariable("lang") String lang);

}
