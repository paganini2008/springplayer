package com.github.paganini2008.springplayer.crumb.repo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import com.github.paganini2008.springplayer.crumb.entity.TraceSpan;

/**
 * 
 * TraceSpanRepo
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public interface TraceSpanRepo extends ElasticsearchRepository<TraceSpan, Long> {
}
