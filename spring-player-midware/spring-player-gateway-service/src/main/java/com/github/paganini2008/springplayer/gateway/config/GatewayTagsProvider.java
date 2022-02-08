package com.github.paganini2008.springplayer.gateway.config;

import java.util.Arrays;

import org.springframework.boot.actuate.metrics.web.reactive.server.WebFluxTags;
import org.springframework.boot.actuate.metrics.web.reactive.server.WebFluxTagsProvider;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.micrometer.core.instrument.Tag;

/**
 * 
 * GatewayTagsProvider
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Primary
@Component
public class GatewayTagsProvider implements WebFluxTagsProvider {

	@Override
	public Iterable<Tag> httpRequestTags(ServerWebExchange exchange, Throwable e) {
		Tag urlTag = Tag.of("uri", exchange.getRequest().getPath().pathWithinApplication().value());
		return Arrays.asList(WebFluxTags.method(exchange), urlTag, WebFluxTags.exception(e), WebFluxTags.status(exchange),
				WebFluxTags.outcome(exchange));
	}

}
