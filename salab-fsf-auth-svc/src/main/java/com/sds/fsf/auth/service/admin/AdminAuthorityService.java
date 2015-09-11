package com.sds.fsf.auth.service.admin;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sds.fsf.auth.domain.Authority;
import com.sds.fsf.auth.domain.User;
import com.sds.fsf.auth.service.AuthorityService;


/**
 * Admin Service class for managing authorities.
 */
@Service
@Transactional
public class AdminAuthorityService extends AuthorityService{

	private final Logger log = LoggerFactory.getLogger(AdminAuthorityService.class);
	
	@Transactional(readOnly=true)
	public Set<Authority> getUserAuthorities(Long id) {
		return super.getUserAuthorities(id);
	}
	
	@Transactional(readOnly=true)
    public Set<Authority> getAuthoritiesGrantedToLoginUnderMyAuthorities(Long id) {
		Set<Authority> authorities = getUserAuthorities(id);
    	authorities.retainAll(getMyGrantableAuthorities());
		return authorities;
	}  	
	
    public boolean saveUserAuthoritiesUnderMyAuthorities(
			Long id, 
			List<Authority> authoritiesToGrant,
			boolean userDeactivedWhenAuthoritiesIsEmpty
			) {
		boolean isChanged = false;
		
		User user = userRepository.findOne(id);
		if(null == user) {
			return false;
		}
		//log.debug("######authoritiesToGrant : " + authoritiesToGrant);
		
		Set<Authority> filterdAuthoritiesToGrant = authorityBaseFilter(user.getAuthorityBase(), authoritiesToGrant);
		//log.debug("######filterdAuthoritiesToGrant : " + filterdAuthoritiesToGrant);
		
		Set<Authority> grantedAuthoritiesFromDB = user.getAuthorities();// getUserAuthorities(id)
		//log.debug("######grantedAuthoritiesFromDB : " + grantedAuthoritiesFromDB);
		
		Set<Authority> authoritiesToBeRemoved = getMyGrantableAuthorities();
		authoritiesToBeRemoved.removeAll(filterdAuthoritiesToGrant);
		isChanged = grantedAuthoritiesFromDB.removeAll(authoritiesToBeRemoved) || isChanged;
		//log.debug("######authoritiesToBeRemoved : " + authoritiesToBeRemoved);
		//log.debug("######grantedAuthoritiesFromDB After unchecked delete : " + grantedAuthoritiesFromDB);
		
		//log.debug("######filterdAuthoritiesToGrant Before check getMyGrantableAuthorities : " + filterdAuthoritiesToGrant);
		//log.debug("######getMyGrantableAuthorities : " + getMyGrantableAuthorities());
		filterdAuthoritiesToGrant.retainAll(getMyGrantableAuthorities());
		//log.debug("######filterdAuthoritiesToGrant After check getMyGrantableAuthorities : " + filterdAuthoritiesToGrant);
		isChanged = grantedAuthoritiesFromDB.addAll(filterdAuthoritiesToGrant) || isChanged;
		//log.debug("######grantedAuthoritiesFromDB After checked add : " + grantedAuthoritiesFromDB);
		
		//user.setAuthorities(grantedAuthoritiesFromDB);
		if(grantedAuthoritiesFromDB.size() == 0 && userDeactivedWhenAuthoritiesIsEmpty) {
			user.setActivated(false);
		}
		userRepository.save(user);
		
		if(isChanged && tokenStore instanceof JdbcTokenStore) {
			JdbcTokenStore jdbcTokenStore = (JdbcTokenStore)tokenStore;
			Collection<OAuth2AccessToken> authentications = jdbcTokenStore.findTokensByUserName(user.getLogin());
			for (OAuth2AccessToken oAuth2AccessToken : authentications) {
				jdbcTokenStore.removeAccessToken(oAuth2AccessToken);
			}
		}
		return true;
	}

    @Transactional(readOnly=true)
	public boolean isDeletableAuthority(String authorityName) {
		return getUsers(authorityName).size() == 0 ? true : false;
	}  
		
}
