package com.github.paganini2008.springplayer.upms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.oauth.WhiteList;
import com.github.paganini2008.springplayer.upms.service.impl.UserManagerService;
import com.github.paganini2008.springplayer.upms.vo.UserVO;

/**
 * 
 * LoginController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@WhiteList
@RequestMapping("/user")
@RestController
public class LoginController {

	@Autowired
	private UserManagerService userManagerService;

	@PostMapping("/info/{username}")
	public ApiResult<UserVO> info(@PathVariable String username) {
		UserVO user = userManagerService.getUserInfo(username);
		return ApiResult.ok(user);
	}

}
