package com.github.paganini2008.springplayer.example.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.paganini2008.springplayer.common.id.IdGenerator;
import com.github.paganini2008.springplayer.common.redis.RedisBloomFilter;
import com.github.paganini2008.springplayer.example.Application;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * TestIdGenerator
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@FixMethodOrder
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
public class TestCommon {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private IdGenerator idGenerator;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Test
	public void testDataSource() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			assert !connection.isClosed();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException ignored) {
				}
			}
		}
	}

	@Test
	public void testIdGenerator() {
		int N = 10000;
		for (int i = 0; i < N; i++) {
			log.info("Generate ID: {}", idGenerator.generateId());
		}
	}

	@Test
	public void testRedisTemplate() {
		String key = "foo";
		String value = "bar";
		redisTemplate.opsForValue().set("foo", value, 15, TimeUnit.SECONDS);
		assert redisTemplate.opsForValue().get(key).equals(value);
	}

	@Test
	public void testBloomFilter() {
		int N = 10000;
		RedisBloomFilter bloomFilter = new RedisBloomFilter("test-bloom", 100000000, 0.03d, redisConnectionFactory);
		String str;
		for (int i = 0; i < N; i++) {
			str = RandomStringUtils.random(32, true, true);
			if (bloomFilter.mightContain(str)) {
				log.info("Duplicated string: {}", str);
			} else {
				bloomFilter.put(str);
			}
		}
	}

}
