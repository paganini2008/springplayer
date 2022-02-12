package com.github.paganini2008.springplayer.channel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.springplayer.channel.model.Channel;
import com.github.paganini2008.springplayer.channel.pojo.ChannelDTO;
import com.github.paganini2008.springplayer.channel.pojo.ChannelQueryDTO;
import com.github.paganini2008.springplayer.channel.pojo.PageWrapper;
import com.github.paganini2008.springplayer.channel.service.ChannelManagerService;
import com.github.paganini2008.springplayer.common.ApiResult;

/**
 * 
 * ChannelManagerController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Validated
@RestController
@RequestMapping("/channel")
public class ChannelManagerController {

	@Autowired
	private ChannelManagerService channelManagerService;

	@PostMapping("/save")
	public ApiResult<String> saveChannel(@Validated @RequestBody ChannelDTO dto) {
		channelManagerService.saveChannel(dto);
		return ApiResult.ok("保存成功");
	}

	@PostMapping("/query")
	public ApiResult<PageWrapper<Channel>> pageForChannel(@Validated @RequestBody ChannelQueryDTO query) {
		PageWrapper<Channel> page = channelManagerService.pageForChannel(query);
		return ApiResult.ok(page);
	}

	@DeleteMapping("/delete/{id}")
	public ApiResult<String> deleteChannel(@PathVariable long id) {
		channelManagerService.deleteChannel(id);
		return ApiResult.ok("删除成功");
	}
}
