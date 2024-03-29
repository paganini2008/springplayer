package com.github.paganini2008.springplayer.common.quartz;

import com.github.paganini2008.devtools.collection.Tuple;

/**
 * 
 * JobParameter
 * 
 * @author Jimmy Hoff
 * @create 2018-03
 */
public interface JobParameter {

	String getName();

	Class<?> getJobClass();

	String getDescription();

	Tuple getTuple();

}