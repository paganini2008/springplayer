package com.github.paganini2008.springplayer.monitor.crumb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.paganini2008.devtools.collection.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * SpanTree
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpanTree implements Serializable {

	private static final long serialVersionUID = -381704678743391063L;

	private Span span;
	private List<SpanTree> branches;

	public static SpanTree load(List<Span> list) {
		Optional<Span> op = list.stream().filter(span -> span.getParentSpanId() == 0).findFirst();
		if (op.isPresent()) {
			return load(op.get(), list);
		}
		return null;
	}

	private static SpanTree load(Span parent, List<Span> list) {
		List<Span> children = list.stream().filter(span -> span.getParentSpanId() == parent.getSpanId()).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(children)) {
			List<SpanTree> branches = new ArrayList<>();
			for (Span span : children) {
				branches.add(load(span, list));
			}
			return new SpanTree(parent, branches);
		}
		return new SpanTree(parent, Collections.emptyList());
	}

}
