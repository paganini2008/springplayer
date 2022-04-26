package com.github.paganini2008.springplayer.common;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.github.paganini2008.devtools.ArrayUtils;

/**
 * 
 * OnApplicationCondition
 *
 * @author Feng Yan
 *
 * @version 2.0.5
 */
public class OnApplicationCondition extends SpringBootCondition {

	private static final ConditionMessage EMPYT_MESSAGE = ConditionMessage.empty();

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnApplication.class.getName());
		String[] applicationNames = (String[]) annotationAttributes.get("value");
		if (applicationNames == null || applicationNames.length == 0) {
			return ConditionOutcome.noMatch(EMPYT_MESSAGE);
		}
		String applicationName = context.getEnvironment().getRequiredProperty("spring.application.name");
		if (ArrayUtils.contains(applicationNames, applicationName)) {
			return ConditionOutcome.match(EMPYT_MESSAGE);
		}
		return ConditionOutcome.noMatch(EMPYT_MESSAGE);
	}

}
