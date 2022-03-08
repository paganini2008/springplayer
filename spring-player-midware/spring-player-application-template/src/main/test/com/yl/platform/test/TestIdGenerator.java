package com.yl.platform.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.paganini2008.springplayer.id.IdGenerator;
import com.yl.platform.example.Application;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * TestIdGenerator
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
public class TestIdGenerator {

	@Autowired
	private IdGenerator idGenerator;

	@Test
	public void testId() {
		for (int i = 0; i < 100; i++) {
			log.info("" + idGenerator.generateId());
		}
	}

}
