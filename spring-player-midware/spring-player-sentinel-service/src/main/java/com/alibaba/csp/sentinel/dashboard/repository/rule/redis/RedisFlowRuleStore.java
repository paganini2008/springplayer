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
package com.alibaba.csp.sentinel.dashboard.repository.rule.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.slots.block.flow.ClusterFlowConfig;

/**
 * 
 * RedisFlowRuleStore
 *
 * @author Fred Feng
 *
 * @version 2.0.5
 */
public class RedisFlowRuleStore extends RedisRuleRepositorySupport<FlowRuleEntity> {

	public RedisFlowRuleStore(RedisTemplate<String, Object> redisTemplate, RedisAtomicLong idGen) {
		super(FlowRuleEntity.class, redisTemplate, idGen);
	}

	@Override
	protected FlowRuleEntity preProcess(FlowRuleEntity entity) {
		if (entity != null && entity.isClusterMode()) {
			ClusterFlowConfig config = entity.getClusterConfig();
			if (config == null) {
				config = new ClusterFlowConfig();
				entity.setClusterConfig(config);
			}
			// Set cluster rule id.
			config.setFlowId(entity.getId());
		}
		return entity;
	}
}
