package com.github.paganini2008.springplayer.common;

import java.util.Collection;

import com.github.paganini2008.devtools.collection.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * PageResult
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageResult<T> {

	private int page;
	private int size;
	private Collection<T> content;

	public boolean isEmpty() {
		return CollectionUtils.isEmpty(content);
	}

}
