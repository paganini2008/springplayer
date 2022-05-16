package org.springframework.cloud.openfeign;

import java.util.Collection;

import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.util.CollectionUtils;

/**
 * 
 * ClassNameTypeFilter
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class ClassNameTypeFilter extends AbstractClassTestingTypeFilter {

	private final Collection<String> classNames;

	public ClassNameTypeFilter(Collection<String> classNames) {
		this.classNames = classNames;
	}

	@Override
	protected boolean match(ClassMetadata metadata) {
		String className = metadata.getClassName();
		return CollectionUtils.isEmpty(classNames) || classNames.contains(className);
	}

}
