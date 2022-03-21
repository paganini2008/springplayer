package com.github.paganini2008.springplayer.common.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * DispatcherJobBean
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class DispatcherJobBean implements Job {

	@Autowired
	private JobDispatcher jobDispatcher;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		final JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		final JobParameter parameter = (JobParameter) dataMap.get("parameter");
		jobDispatcher.dispatch(parameter);
	}

}
