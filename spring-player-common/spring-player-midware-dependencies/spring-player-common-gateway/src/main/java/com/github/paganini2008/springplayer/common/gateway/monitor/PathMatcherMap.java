package com.github.paganini2008.springplayer.common.gateway.monitor;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.github.paganini2008.devtools.collection.KeyMatchedMap;

/**
 * 
 * PathMatcherMap
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class PathMatcherMap<V> extends KeyMatchedMap<String, V> {

	private static final long serialVersionUID = -8496417385491734777L;

	public PathMatcherMap() {
		super(new ConcurrentHashMap<>(), false);
	}

	private final PathMatcher pathMather = new AntPathMatcher();

	@Override
	protected boolean match(String pathPattern, Object inputKey) {
		String path = (String) inputKey;
		return pathPattern.equals(path) || pathMather.match(pathPattern, path);
	}

	public static void main(String[] args) {
		PathMatcherMap<String> pathMatcherMap = new PathMatcherMap<String>();
		pathMatcherMap.putIfAbsent("/**", "100");
		pathMatcherMap.putIfAbsent("/a/**", "200");
		pathMatcherMap.putIfAbsent("/a/b/**", "300");
		System.out.println(pathMatcherMap);
		System.out.println(pathMatcherMap.containsKey("/c/d/e"));
		System.out.println(pathMatcherMap.existsKey("/a/**"));
	}

}
