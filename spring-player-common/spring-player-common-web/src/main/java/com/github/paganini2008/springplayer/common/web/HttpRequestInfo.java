package com.github.paganini2008.springplayer.common.web;

import java.io.Serializable;

import org.springframework.http.HttpHeaders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * HttpRequestInfo
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpRequestInfo implements Serializable {

	private static final long serialVersionUID = 5359161967793485293L;
	private String method;
	private String path;
	private HttpHeaders headers = new HttpHeaders();

}
