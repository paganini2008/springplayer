package com.github.paganini2008.springplayer.common.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paganini2008.devtools.jdbc.ResultSetSlice;

/**
 * 
 * PageableIService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface PageableIService<T> extends IService<T> {

	ResultSetSlice<T> slice(LambdaQueryWrapper<T> queryWrapper);

}
