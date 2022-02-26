package com.github.paganini2008.springplayer.applog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.springplayer.applog.service.AppLogService;
import com.github.paganini2008.springplayer.applog.service.SearchFilterQuery;
import com.github.paganini2008.springplayer.applog.vo.AppLogVO;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.PageResult;

/**
 * 
 * AppLogController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RequestMapping("/applog")
@RestController
public class AppLogController {

	@Autowired
	private AppLogService appLogService;

	@GetMapping("/search")
	public ApiResult<PageResult<AppLogVO>> search(@RequestParam("q") String keyword) {
		PageResult<AppLogVO> pageResult = appLogService.searchForPage(keyword, new SearchFilterQuery());
		return ApiResult.ok(pageResult);
	}

	@PostMapping("/search")
	public ApiResult<PageResult<AppLogVO>> search(@RequestParam("q") String keyword, @RequestBody SearchFilterQuery query) {
		PageResult<AppLogVO> pageResult = appLogService.searchForPage(keyword, query);
		return ApiResult.ok(pageResult);
	}

}
