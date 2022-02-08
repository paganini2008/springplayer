package com.github.paganini2008.springplayer.sysinfo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.springplayer.common.ApiResult;

/**
 * 
 * SysInfoEndpoint
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@ConditionalOnWebApplication(type = Type.SERVLET)
@RequestMapping("/sys")
@RestController
public class SysInfoEndpoint {

	@PostMapping("/info")
	public ApiResult<ServerInfo> getServerInfo() throws Exception {
		ServerInfo serverInfo = new ServerInfo();
		serverInfo.refresh();
		return ApiResult.ok(serverInfo);
	}

}
