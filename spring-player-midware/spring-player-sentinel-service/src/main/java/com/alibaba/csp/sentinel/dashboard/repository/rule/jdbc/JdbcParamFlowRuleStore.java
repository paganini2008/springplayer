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
package com.alibaba.csp.sentinel.dashboard.repository.rule.jdbc;

import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowClusterConfig;

/**
 * 
 * JdbcParamFlowRuleStore
 *
 * @author Feng Yan
 *
 * @version 2.0.5
 */
public class JdbcParamFlowRuleStore extends JdbcRuleRepositorySupport<ParamFlowRuleEntity> {

	public JdbcParamFlowRuleStore(SentinelRuleService sentinelRuleService, RedisAtomicLong idGen) {
		super(ParamFlowRuleEntity.class, sentinelRuleService, idGen);
	}

	@Override
	protected ParamFlowRuleEntity preProcess(ParamFlowRuleEntity entity) {
		if (entity != null && entity.isClusterMode()) {
			ParamFlowClusterConfig config = entity.getClusterConfig();
			if (config == null) {
				config = new ParamFlowClusterConfig();
			}
			// Set cluster rule id.
			config.setFlowId(entity.getId());
		}
		return entity;
	}
}
