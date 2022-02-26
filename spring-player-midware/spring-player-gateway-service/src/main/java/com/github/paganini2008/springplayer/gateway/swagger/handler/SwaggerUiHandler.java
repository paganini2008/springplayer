
package com.github.paganini2008.springplayer.gateway.swagger.handler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

/**
 * 
 * SwaggerUiHandler
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
public class SwaggerUiHandler implements HandlerFunction<ServerResponse> {

	@Autowired(required = false)
	private UiConfiguration uiConfiguration;

	@Override
	public Mono<ServerResponse> handle(ServerRequest request) {
		return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(Optional.ofNullable(uiConfiguration).orElse(UiConfigurationBuilder.builder().build())));
	}

}
