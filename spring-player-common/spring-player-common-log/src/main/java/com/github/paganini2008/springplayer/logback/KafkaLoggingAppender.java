package com.github.paganini2008.springplayer.logback;

import static com.github.paganini2008.springplayer.common.Constants.KAFKA_TOPIC_APPLOG;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_SPAN_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_HEADER_TRACE_ID;
import static com.github.paganini2008.springplayer.common.Constants.REQUEST_PATH;

import java.io.StringReader;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.devtools.SystemPropertyUtils;
import com.github.paganini2008.devtools.collection.MapUtils;
import com.github.paganini2008.devtools.io.LineIterator;
import com.github.paganini2008.devtools.net.NetUtils;
import com.github.paganini2008.springplayer.logging.common.AppLogEntry;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * 
 * KafkaLoggingAppender
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class KafkaLoggingAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

	private String clusterName = "default";
	private String applicationName = "default";
	private String host = getHost();

	private String topic = KAFKA_TOPIC_APPLOG;
	private String kafkaSettings;
	private Properties kafkaProperties = new Properties();

	private KafkaProducer<String, AppLogEntry> producer;

	public KafkaLoggingAppender() {
		kafkaProperties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		kafkaProperties.setProperty("value.serializer", "com.github.paganini2008.springplayer.logging.common.AppLogEntryJsonSerializer");
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setKafkaSettings(String kafkaSettings) {
		this.kafkaSettings = kafkaSettings;
	}

	@Override
	public final void start() {
		if (producer == null) {
			initializeKafkaProducer();
		}
		super.start();
	}

	private void initializeKafkaProducer() {
		if (StringUtils.isBlank(kafkaSettings)) {
			throw new IllegalArgumentException("Please set kafka properties first");
		}
		LineIterator iterator = new LineIterator(new StringReader(kafkaSettings));
		String line;
		String[] keyVal;
		while (iterator.hasNext()) {
			line = iterator.next();
			if (StringUtils.isBlank(line)) {
				continue;
			}
			keyVal = line.split("=", 2);
			if (keyVal.length > 1) {
				kafkaProperties.putIfAbsent(keyVal[0].trim(), keyVal[1].trim());
			}
		}
		producer = new KafkaProducer<>(kafkaProperties);
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		Map<String, String> mdc = eventObject.getMDCPropertyMap();
		if (MapUtils.isEmpty(mdc) || !mdc.containsKey(REQUEST_HEADER_TRACE_ID)) {
			return;
		}
		String traceId = mdc.get(REQUEST_HEADER_TRACE_ID);
		int span = Integer.parseInt(mdc.get(REQUEST_HEADER_SPAN_ID));
		String requestPath = mdc.get(REQUEST_PATH);
		
		AppLogEntry logEntry = new AppLogEntry();
		logEntry.setTraceId(traceId);
		logEntry.setSpan(span);
		logEntry.setIdentifier(requestPath);

		logEntry.setClusterName(clusterName);
		logEntry.setApplicationName(applicationName);
		logEntry.setHost(host);
		logEntry.setLoggerName(eventObject.getLoggerName());
		logEntry.setMessage(eventObject.getFormattedMessage());
		logEntry.setLevel(eventObject.getLevel().toString());
		if (eventObject.getThrowableProxy() != null) {
			logEntry.setError(ThrowableProxyUtil.asString(eventObject.getThrowableProxy()));
		} else {
			logEntry.setError("<None>");
		}
		logEntry.setMarker(eventObject.getMarker() != null ? eventObject.getMarker().getName() : "<None>");
		logEntry.setCreateTime(new Date(eventObject.getTimeStamp()));

		producer.send(new ProducerRecord<String, AppLogEntry>(topic, logEntry));

	}

	private static String getHost() {
		String localAddress = NetUtils.getLocalHost();
		Integer port = SystemPropertyUtils.getInteger("server.port");
		if (port != null && port.intValue() > 0) {
			return localAddress + ":" + port;
		}
		return localAddress;
	}

}
