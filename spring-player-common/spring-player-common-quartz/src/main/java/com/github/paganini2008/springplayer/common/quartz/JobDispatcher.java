package com.github.paganini2008.springplayer.common.quartz;

/**
 * 
 * JobDispatcher
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public interface JobDispatcher {

	static final String DEFAULT_JOB_INVOCATION = "execute";

	void dispatch(JobParameter jobParameter);

}
