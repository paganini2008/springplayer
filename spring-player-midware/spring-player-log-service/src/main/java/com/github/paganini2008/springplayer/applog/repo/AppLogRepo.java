package com.github.paganini2008.springplayer.applog.repo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import com.github.paganini2008.springplayer.applog.entity.AppLog;

/**
 * 
 * AppLogRepo
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public interface AppLogRepo extends ElasticsearchRepository<AppLog, Long> {
}
