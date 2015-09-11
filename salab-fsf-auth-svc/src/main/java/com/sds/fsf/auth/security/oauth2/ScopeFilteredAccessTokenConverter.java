package com.sds.fsf.auth.security.oauth2;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

public class ScopeFilteredAccessTokenConverter extends DefaultAccessTokenConverter
		implements AccessTokenConverter {

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ?> convertAccessToken(OAuth2AccessToken token,
			OAuth2Authentication authentication) {
		Map<String, Object> tokenMap = (Map<String, Object>) super.convertAccessToken(token, authentication);
		
		if(tokenMap.get(UserAuthenticationConverter.AUTHORITIES) instanceof Set) {
			Set<String> authoritiesSet = (Set<String>) tokenMap.get(UserAuthenticationConverter.AUTHORITIES);
			Set<String> scopeFilteredAuthoritiesSet = new HashSet<String>();		
			for (String authority : authoritiesSet) {
				boolean isInScope = false;
				for (String scope : token.getScope()) {
					if(authority.equals(scope) || authority.startsWith(scope + "_")) {
						isInScope = true;
					}
				}
				if(isInScope) {
					scopeFilteredAuthoritiesSet.add(authority);
				}
			}	
			tokenMap.put(UserAuthenticationConverter.AUTHORITIES, scopeFilteredAuthoritiesSet);
		}

		return tokenMap;
	}
	
}
