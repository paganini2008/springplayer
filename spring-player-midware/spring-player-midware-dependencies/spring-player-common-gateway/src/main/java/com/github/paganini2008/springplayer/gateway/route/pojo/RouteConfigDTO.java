package com.github.paganini2008.springplayer.gateway.route.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 
 * RouteConfigDTO
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Data
public class RouteConfigDTO {

	@NotBlank(message = "应用服务名不能为空")
	private String id;

	private List<NameArgs> predicates = new ArrayList<>();

	private List<NameArgs> filters = new ArrayList<>();

	@NotBlank(message = "请求根路径不能为空")
	private String uri;

	private Map<String, Object> metadata = new HashMap<>();

	private int order = 0;

	public RouteConfigDTO() {
	}

	@Data
	public static class NameArgs {

		private String text;
		private String name;
		private Map<String, String> args;

		public NameArgs() {
		}

		public NameArgs(String text) {
			this.text = text;
		}

		public NameArgs(String name, Map<String, String> args) {
			this.name = name;
			this.args = args;
		}

	}

}
