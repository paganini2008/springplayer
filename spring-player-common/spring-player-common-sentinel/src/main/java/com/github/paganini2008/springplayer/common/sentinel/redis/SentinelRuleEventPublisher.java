package com.github.paganini2008.springplayer.common.sentinel.redis;

import static com.github.paganini2008.springplayer.common.sentinel.SentinelConstants.REDIS_PUBSUB_SENTINEL_RULE_PUBLISH;
import static com.github.paganini2008.springplayer.common.sentinel.SentinelConstants.REDIS_PUBSUB_SENTINEL_RULE_UPDATE;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.github.paganini2008.springplayer.common.redis.pubsub.RedisMessageEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * SentinelRuleEventPublisher
 *
 * @author Feng Yan
 * @version 1.0.0
 */
@Slf4j
@Component
public class SentinelRuleEventPublisher implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher applicationEventPublisher;

	@EventListener(RedisMessageEvent.class)
	public void handleRedisMessageEvent(RedisMessageEvent event) {
		switch (event.getChannel()) {
		case REDIS_PUBSUB_SENTINEL_RULE_PUBLISH:
			doPublishSentinelRule(event);
			break;
		case REDIS_PUBSUB_SENTINEL_RULE_UPDATE:
			doUpdateSentinelRule(event);
			break;
		default:
			break;
		}
	}

	private void doPublishSentinelRule(RedisMessageEvent event) {
		SentinelRuleKeys sentinelRuleKeys = (SentinelRuleKeys) event.getMessage();
		this.applicationEventPublisher
				.publishEvent(new SentinelRulePublishEvent(this, sentinelRuleKeys.getRuleType(), sentinelRuleKeys.getRuleKeys()));
		log.info("发布Sentinel规则信息： " + sentinelRuleKeys.toString());
	}

	private void doUpdateSentinelRule(RedisMessageEvent event) {
		SentinelRuleKeys sentinelRuleKeys = (SentinelRuleKeys) event.getMessage();
		this.applicationEventPublisher
				.publishEvent(new SentinelRuleUpdateEvent(this, sentinelRuleKeys.getRuleType(), sentinelRuleKeys.getRuleKeys()));
		log.info("更新Sentinel规则信息： " + sentinelRuleKeys.toString());
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
}
