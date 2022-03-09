package com.github.paganini2008.springplayer.sentinel.redis;

import static com.github.paganini2008.springplayer.sentinel.SentinelConstants.REDIS_PUBSUB_CHANNEL_SENTINEL_RULE_PUBLISH;
import static com.github.paganini2008.springplayer.sentinel.SentinelConstants.REDIS_PUBSUB_CHANNEL_SENTINEL_RULE_UPDATE;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.github.paganini2008.springplayer.common.JacksonUtils;
import com.github.paganini2008.springplayer.common.redis.pubsub.RedisPubSub;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * SentinelRuleEventPublisher
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
public class SentinelRuleEventPublisher implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher;

	@RedisPubSub(REDIS_PUBSUB_CHANNEL_SENTINEL_RULE_PUBLISH)
	public void publishSentinelRulePublishEvent(String channel, Object message) {
		SentinelRuleKeys sentinelRuleKeys = JacksonUtils.parseJson((String) message, SentinelRuleKeys.class);
		this.applicationEventPublisher
				.publishEvent(new SentinelRulePublishEvent(this, sentinelRuleKeys.getRuleType(), sentinelRuleKeys.getRuleKeys()));
		log.info("发布Sentinel规则信息： " + message);
	}

	@RedisPubSub(REDIS_PUBSUB_CHANNEL_SENTINEL_RULE_UPDATE)
	public void publicSentinelRuleUpdateEvent(String channel, Object message) {
		SentinelRuleKeys sentinelRuleKeys = JacksonUtils.parseJson((String) message, SentinelRuleKeys.class);
		this.applicationEventPublisher
				.publishEvent(new SentinelRuleUpdateEvent(this, sentinelRuleKeys.getRuleType(), sentinelRuleKeys.getRuleKeys()[0]));
		log.info("更新Sentinel规则信息： " + message);
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
}
