package com.github.paganini2008.springplayer.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.paganini2008.devtools.jdbc.ResultSetSlice;

/**
 * 
 * PageableIServiceImpl
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class PageableIServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements PageableIService<T> {

	@Override
	public ResultSetSlice<T> slice(LambdaQueryWrapper<T> queryWrapper) {
		return new MyBatisResultSetSlice<T>(queryWrapper, this);
	}

}
