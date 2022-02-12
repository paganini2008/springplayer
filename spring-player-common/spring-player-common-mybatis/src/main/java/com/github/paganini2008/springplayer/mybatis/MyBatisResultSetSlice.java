package com.github.paganini2008.springplayer.mybatis;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paganini2008.devtools.jdbc.ResultSetSlice;

import lombok.RequiredArgsConstructor;

/**
 * 
 * MyBatisResultSetSlice
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class MyBatisResultSetSlice<T> implements ResultSetSlice<T> {

	private static final String LAST_SQL_TEMPLATE = "limit %s offset %s";

	private final LambdaQueryWrapper<T> wrapper;
	private final IService<T> service;

	@Override
	public List<T> list(int maxResults, int firstResult) {
		wrapper.last(String.format(LAST_SQL_TEMPLATE, maxResults, firstResult));
		return service.list(wrapper);
	}

	@Override
	public int rowCount() {
		return (int) service.count(wrapper);
	}

}
