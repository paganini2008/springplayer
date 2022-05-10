package com.github.paganini2008.springplayer.applog.utils;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * 
 * KafkaProducerTester
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class KafkaProducerTester {

	private final KafkaProducer<String, String> producer;

	public final static String TOPIC = "test123";

	private KafkaProducerTester() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.159.1:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		producer = new KafkaProducer<String, String>(props);
	}

	public void produce() {
		int messageNo = 1;
		final int COUNT = 10000;

		while (messageNo < COUNT) {
			String key = String.valueOf(messageNo);
			String data = String.format("Kafka test message %s at %s", key, new Date());

			try {
				producer.send(new ProducerRecord<String, String>(TOPIC, data));
				System.out.println("发送： " + data);
			} catch (Exception e) {
				e.printStackTrace();
			}

			messageNo++;
		}

		producer.close();
	}

	public static void main(String[] args) throws IOException {
		new KafkaProducerTester().produce();
		System.in.read();
		System.out.println("Over");
	}
}
