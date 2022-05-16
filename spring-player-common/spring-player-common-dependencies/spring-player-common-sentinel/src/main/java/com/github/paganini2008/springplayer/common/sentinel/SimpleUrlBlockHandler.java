package com.github.paganini2008.springplayer.common.sentinel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.github.paganini2008.springplayer.common.ApiResult;
import com.github.paganini2008.springplayer.common.utils.JacksonUtils;

/**
 * 
 * SimpleUrlBlockHandler
 *
 * @author Fred Feng
 *
 * @version 2.0.5
 */
public class SimpleUrlBlockHandler implements BlockExceptionHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
		String msg = "";
		if (e instanceof AuthorityException) {
			msg = "权限不够，无法访问";
		} else if (e instanceof DegradeException) {
			msg = "接口错误，已降级";
		} else if (e instanceof FlowException) {
			msg = "接口访问过快，已限流";
		} else if (e instanceof ParamFlowException) {
            msg = "出现热点参数，已限流";
        } else if (e instanceof SystemBlockException) {
            msg = "系统过载，已限流";
        }
		response.setStatus(500);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		
		JacksonUtils.writeObject(response.getWriter(), ApiResult.failed(msg, null));
		
	}

}
