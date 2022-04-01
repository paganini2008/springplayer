package com.github.paganini2008.springplayer.common.ws;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.java_websocket.handshake.ServerHandshake;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * WsConnectionWrapper
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class WsConnectionWrapper implements WsStateListener, WsConnection {

	private WsClientHolder connection;
	private final int maxRetries;
	private final List<String> failedQueue = new CopyOnWriteArrayList<>();

	public WsConnectionWrapper(String url, int maxRetries) {
		this.connection = new WsClientHolder(URI.create(url), this);
		this.maxRetries = maxRetries;
	}

	private volatile boolean abandoned;
	private volatile int retry = 0;

	public void connect() {
		try {
			connection.connectBlocking(3, TimeUnit.SECONDS);
		} catch (InterruptedException ignored) {
		}
	}

	public void close(boolean abandoned) {
		this.abandoned = abandoned;
		connection.close();
	}

	public boolean isClosed() {
		return connection.isClosed();
	}

	public boolean isAbandoned() {
		return abandoned;
	}

	public void send(String text) {
		if (connection.isOpen()) {
			connection.send(text);
		} else {
			failedQueue.add(text);
		}
	}

	@Override
	public void onOpen(ServerHandshake handshake) {
		if (log.isInfoEnabled()) {
			log.info("Connnection is open. Status: {}, Message: {}", handshake.getHttpStatus(), handshake.getHttpStatusMessage());
		}
		retry = 0;
		for (String text : failedQueue) {
			send(text);
			failedQueue.remove(text);
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		if (log.isInfoEnabled()) {
			log.info("Connnection is closed. Code: {}, Reason: {}, Remote: {}", code, reason, remote);
		}
		if (!abandoned) {
			if (retry < maxRetries) {
				connection = new WsClientHolder(connection.getURI(), this);
				connection.connect();
				retry++;
			} else {
				abandoned = true;
			}
		}
	}

	@Override
	public void onError(Exception e) {
		if (log.isErrorEnabled()) {
			log.error(e.getMessage(), e);
		}
		if (!abandoned) {
			if (retry < maxRetries) {
				connection = new WsClientHolder(connection.getURI(), this);
				connection.connect();
				retry++;
			} else {
				abandoned = true;
			}
		}
	}

}
