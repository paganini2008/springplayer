package com.github.paganini2008.springplayer.common.ws;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.paganini2008.devtools.collection.MapUtils;

/**
 * 
 * WsConnectionPool
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class WsConnectionPool {

	private final Map<String, WsConnection> pool = new ConcurrentHashMap<>();

	public WsConnection get(String url) {
		return MapUtils.get(pool, url, () -> {
			WsConnectionWrapper connection = new WsConnectionWrapper(url, 3);
			connection.connect();
			return connection;
		});
	}

	public void remove(String url) {
		WsConnection connection = pool.remove(url);
		if (connection != null && !connection.isClosed()) {
			connection.close(true);
		}
	}

	public int size() {
		return pool.size();
	}

}
