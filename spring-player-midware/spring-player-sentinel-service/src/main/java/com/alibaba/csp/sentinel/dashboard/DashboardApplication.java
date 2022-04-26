/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthIndicatorAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.alibaba.csp.sentinel.init.InitExecutor;

/**
 * Sentinel dashboard application.
 *
 * @author Carpenter Lee
 */
@EnableEurekaClient
@SpringBootApplication(exclude = { DataSourceHealthIndicatorAutoConfiguration.class })
public class DashboardApplication {

	static {
		System.setProperty("management.endpoints.web.exposure.include", "*");
		System.setProperty("management.endpoint.health.show-details", "always");
	}

	public static void main(String[] args) {
		triggerSentinelInit();
		SpringApplication.run(DashboardApplication.class, args);
	}

	private static void triggerSentinelInit() {
		new Thread(() -> InitExecutor.doInit()).start();
	}
}
