package com.github.paganini2008.springplayer.crumb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.PageResult;
import com.github.paganini2008.springplayer.crumb.service.SearchFilterQuery;
import com.github.paganini2008.springplayer.crumb.service.TraceSpanService;
import com.github.paganini2008.springplayer.crumb.vo.TraceSpanVO;

/**
 * 
 * TraceSpanController
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RequestMapping("/trace")
@RestController
public class TraceSpanController {

	@Autowired
	private TraceSpanService traceSpanService;

	@PostMapping("/search")
	public ApiResult<PageResult<TraceSpanVO>> searchForPage(@RequestBody SearchFilterQuery query) {
		return ApiResult.ok(traceSpanService.searchForPage(query));
	}

}
