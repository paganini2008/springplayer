package com.github.paganini2008.springplayer.common.gateway.route;

import org.springframework.context.ApplicationEvent;

/**
 * 
 * RoutePublishEvent
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class RoutePublishEvent extends ApplicationEvent {

	private static final long serialVersionUID = 642636779586320846L;

	public RoutePublishEvent(Object source, String groupName) {
		super(source);
		this.groupName = groupName;
	}

	private final String groupName;

	public String getGroupName() {
		return groupName;
	}

}
