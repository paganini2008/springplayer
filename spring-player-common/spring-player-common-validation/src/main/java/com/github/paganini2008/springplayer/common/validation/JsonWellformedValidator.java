package com.github.paganini2008.springplayer.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.paganini2008.devtools.StringUtils;
import com.github.paganini2008.springplayer.common.utils.JacksonUtils;

/**
 * 
 * JsonWellformedValidator
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class JsonWellformedValidator implements ConstraintValidator<JsonWellformed, String> {

	private Class<?> testClass;

	@Override
	public void initialize(JsonWellformed anno) {
		this.testClass = anno.test();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isNotBlank(value)) {
			try {
				JacksonUtils.parseJson(value, testClass);
			} catch (RuntimeException e) {
				return false;
			}
		}
		return true;
	}

}
