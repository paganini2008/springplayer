
package com.github.paganini2008.springplayer.common.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * CheckedBearerTokenExtractor
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class CheckedBearerTokenExtractor extends BearerTokenExtractor {

	private final PathMatcher pathMatcher = new AntPathMatcher();

	private final WhiteListProperties whiteListProperties;

	@Override
	public Authentication extract(HttpServletRequest request) {
		boolean match = whiteListProperties.getWhiteListUrls().stream().anyMatch(
				url -> url.equals(request.getRequestURI()) || pathMatcher.match(url, request.getRequestURI()));
		return match ? null : super.extract(request);
	}

}
