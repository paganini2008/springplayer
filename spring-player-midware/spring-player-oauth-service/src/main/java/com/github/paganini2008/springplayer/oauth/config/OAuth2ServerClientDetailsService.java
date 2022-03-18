package com.github.paganini2008.springplayer.oauth.config;

import javax.sql.DataSource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import com.github.paganini2008.springplayer.common.oauth.ErrorCodes;
import com.github.paganini2008.springplayer.common.oauth.info.OauthConstants;
import com.github.paganini2008.springplayer.common.web.HttpRequestContextHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * OAuth2ServerClientDetailsService
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Slf4j
@Service
public class OAuth2ServerClientDetailsService extends JdbcClientDetailsService {

	public OAuth2ServerClientDetailsService(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	@Cacheable(value = "oauth:client:details", key = "#clientId", unless = "#result == null")
	public ClientDetails loadClientByClientId(String clientId) {
		String sql = String.format(OauthConstants.DEFAULT_SELECT_STATEMENT, HttpRequestContextHolder.getTenantId());
		super.setSelectClientDetailsSql(sql);
		try {
			return super.loadClientByClientId(clientId);
		} catch (ClientRegistrationException e) {
			log.error(e.getMessage(), e);
			throw new OAuth2ServerException(ErrorCodes.BAD_CLIENT_CREDENTIALS);
		}
	}

}
