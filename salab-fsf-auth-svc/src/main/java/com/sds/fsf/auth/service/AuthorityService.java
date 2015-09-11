package com.sds.fsf.auth.service;

import com.sds.fsf.cmm.security.util.SecurityUtils;
import com.sds.fsf.cmm.web.rest.util.PaginationUtil;
import com.sds.fsf.auth.domain.Authority;
import com.sds.fsf.auth.domain.Client;
import com.sds.fsf.auth.domain.User;
import com.sds.fsf.auth.repository.AuthorityRepository;
import com.sds.fsf.auth.repository.ClientRepository;
import com.sds.fsf.auth.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service class for managing authorities.
 */
@Service
@Transactional
public class AuthorityService {

    private final Logger log = LoggerFactory.getLogger(AuthorityService.class);

    @Inject
    protected AuthorityRepository authorityRepository;
    
	@Inject
	protected ClientRepository clientRepository;   
	
    @Inject
    protected UserRepository userRepository;
    
    @Inject
    protected TokenStore tokenStore;    	
	
	protected static final String ROLE_DELIMITER_WITH_ESCAPE = "\\_";
	
	protected static final String ROLE_DELIMITER = "_";
	
    protected static final String SUB_ROLE_REGEX_PREFIX = "^";
    
    protected static final String SUB_ROLE_REGEX_SUFFIX = "_([A-Z][A-Z0-9]*)(_[A-Z][A-Z0-9]*)*$";
    
    protected static final String getSubAuthorityNameRegex(String authorityName) {
    	return SUB_ROLE_REGEX_PREFIX + authorityName + SUB_ROLE_REGEX_SUFFIX;
    }
    
    protected static final String ROLE_REGEX= "^ROLE_([A-Z][A-Z0-9]*)(_[A-Z][A-Z0-9]*)*$";
    
    protected static final boolean isAuthorityName(String authorityName) {
    	if(null == authorityName) {
    		return false;
    	}
    	return authorityName.matches(ROLE_REGEX);
    }      
 
    @Transactional(readOnly=true)
	protected Set<User> getUsers(String authorityName) {
        Authority authority = authorityRepository.findOne(authorityName);
        Set<User> users = new HashSet<User>();
        if(null == authority) {
        	return users;
        }
        authority.getUsers().size(); // eagerly load the association
        users.addAll(authority.getUsers());
        return users;
    }

    @Transactional(readOnly=true)
    protected Set<User> getActiveUsers(String authorityName) {
    	Set<User> users = getUsers(authorityName);
    	Set<User> activeUsers = new HashSet<User>();
    	for (User user : users) {
			if(user.getActivated()) activeUsers.add(user);
		}
		return activeUsers;
	}   
    
    @Transactional(readOnly=true)
    protected Set<Authority> getUserAuthorities(Long userId) {
    	Set<Authority> userAuthorites = new HashSet<Authority>();
        User currentUser = userRepository.findOne(userId);
        if(null == currentUser) {
        	return userAuthorites;
        }
        currentUser.getAuthorities().size(); // eagerly load the association
        userAuthorites.addAll(currentUser.getAuthorities());
        return userAuthorites;
    } 

    @Transactional(readOnly=true)
    protected Set<Authority> getUserAuthorities(String login) {
    	Set<Authority> userAuthorites = new HashSet<Authority>();
        User currentUser = userRepository.findOneByLogin(login);
        if(null == currentUser) {
        	return userAuthorites;
        }
        currentUser.getAuthorities().size(); // eagerly load the association
        userAuthorites.addAll(currentUser.getAuthorities());
        return userAuthorites;
    }
 
    @Transactional(readOnly=true)
    public Set<Authority> authorityBaseFilter(String authorityBase, Collection<Authority> authoritiesToGrant) {
    	Set<Authority> filterdAuthoritiesToGrant = new HashSet<Authority>();
    	if(StringUtils.isEmpty(authorityBase)) {
    		filterdAuthoritiesToGrant.addAll(authoritiesToGrant);
    		return filterdAuthoritiesToGrant;
    	}	
		for (Authority authority : authoritiesToGrant) {
			if(isUnderAuthorityBaseOf(authorityBase, authority.getName())){
				filterdAuthoritiesToGrant.add(authority);
			}
		}
    	return filterdAuthoritiesToGrant;
    }
    
    public boolean saveUserAuthoritiesUnderMyAuthorities(
			String login, 
			List<Authority> authoritiesToGrant,
			boolean userDeactivedWhenAuthoritiesIsEmpty
			) {
		boolean isChanged = false;
		
		User user = userRepository.findOneByLogin(login);
		if(null == user) {
			return false;
		}
		
		Set<Authority> filterdAuthoritiesToGrant = authorityBaseFilter(user.getAuthorityBase(), authoritiesToGrant);
		
		//Long id = user.getId();
		Set<Authority> grantedAuthoritiesFromDB = user.getAuthorities();//getUserAuthorities(id);
		
		Set<Authority> authoritiesToBeRemoved = getMyGrantableAuthorities();
		authoritiesToBeRemoved.removeAll(filterdAuthoritiesToGrant);
		isChanged = grantedAuthoritiesFromDB.removeAll(authoritiesToBeRemoved) || isChanged;
		
		filterdAuthoritiesToGrant.retainAll(getMyGrantableAuthorities());
		isChanged = grantedAuthoritiesFromDB.addAll(filterdAuthoritiesToGrant) || isChanged;
		
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
    public Set<Authority> getMyGrantableAuthorities() {
    	Set<Authority> grantableAuthorities = new HashSet<Authority>();
    	Set<Authority> grantedAuthorities = getMyGrantedAuthorities();
    	for (Authority grantedAuthority : grantedAuthorities) {
    		String prefix = grantedAuthority.getName() + ROLE_DELIMITER_WITH_ESCAPE;
    		List<Authority> resultOfStartingWith = authorityRepository.findByNameStartingWith(prefix);
    		grantableAuthorities.addAll(resultOfStartingWith);
		} 	
    	return grantableAuthorities;
    }
    
    @Transactional(readOnly=true)
    public Set<Authority> getMyGrantedAuthorities() {
    	Set<Authority> grantedAuthorities = new HashSet<Authority>();
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if(authentication.getPrincipal() instanceof UserDetails) {
    		String username = ((UserDetails)authentication.getPrincipal()).getUsername();
    		User user = userRepository.findOneByLogin(username);
    		if(null != user) {
    			grantedAuthorities.addAll(getUserAuthorities(user.getId()));
    		} 	
    	}
    	return grantedAuthorities;    	
    }  
    
    @Transactional(readOnly=true)
    public boolean isValidNameUnderMyGrantedAuthorities(String authorityName) {
		boolean isValidName = false;
		Set<Authority> grantedAuthorities = getMyGrantedAuthorities();
		for (Authority grantedAuthority : grantedAuthorities) {
			String subAuthorityNameRegex = getSubAuthorityNameRegex(grantedAuthority.getName());
			if(isAuthorityName(authorityName) && authorityName.matches(subAuthorityNameRegex)){
				isValidName = true;
				break;
			}
		}
		return isValidName;
	}
    
    @Transactional(readOnly=true)
    public boolean isUnderAuthorityBaseOf(String authorityBase, String authorityName) {
		String subAuthorityNameRegex = getSubAuthorityNameRegex(authorityBase);
		return isAuthorityName(authorityName) && authorityName.matches(subAuthorityNameRegex);
    }
    
    @Transactional(readOnly=true)
    public Set<Authority> getAuthoritiesGrantedToLoginUnderMyAuthorities(String login) {
		Set<Authority> authorities = new HashSet<Authority>(getUserAuthorities(login));
    	authorities.retainAll(getMyGrantableAuthorities());
		return authorities;
	}  
    
    @Transactional(readOnly=true)
    public Set<Authority> getAuthoritiesUnderMyAuthorities(String authorityName, boolean includeSub) {
		Set<Authority> authorities = new HashSet<Authority>();
		boolean isUnderMine = isValidNameUnderMyGrantedAuthorities(authorityName);
		boolean isMine = SecurityUtils.isUserInRole(authorityName);
    	if(isUnderMine || isMine) {
			Authority authority = authorityRepository.findOne(authorityName);
			if(null != authority) {
				if(isUnderMine) {
					authorities.add(authority);
				}	
	    		if(includeSub) {
	    			List<Authority> subAuthorities = authorityRepository.findByNameStartingWith(authorityName + ROLE_DELIMITER_WITH_ESCAPE);
	    			authorities.addAll(subAuthorities);
	    		}				
			}
    	}
		return authorities;
	}  
    
    @Transactional(readOnly=true)
	public ArrayList<Map<String, Object>> getUsersHaveAuthoritiesUnderMyAuthorities(
			String authorityName, boolean includeSub, boolean activeOnly) {
		Set<Authority> authorities = getAuthoritiesUnderMyAuthorities(authorityName, includeSub);
    	ArrayList<Map<String, Object>> authoritiesAndUsersMapList = new ArrayList<Map<String, Object>>();
		for (Authority authority : authorities) {
			Map<String, Object> authoritiesAndUsersMap = new HashMap<String, Object>();
			authoritiesAndUsersMap.put("authority", authority);
			Set<User> usersHaveAuthority;
			if(!activeOnly) {
				usersHaveAuthority = getUsers(authority.getName());
			} else {
				usersHaveAuthority = getActiveUsers(authority.getName());
			}
	    	authoritiesAndUsersMap.put("users", usersHaveAuthority);
	    	authoritiesAndUsersMapList.add(authoritiesAndUsersMap);
		}
		return authoritiesAndUsersMapList;
	}
	
    @Transactional(readOnly=true)
    public Set<Authority> getClientGrantDefaultUserAuthorities(String clientId) {
    	Set<Authority> ClientGrantDefaultUserAuthorities = new HashSet<Authority>();
        Client client = clientRepository.findOne(clientId);
        if(null == client) {
        	return ClientGrantDefaultUserAuthorities;
        }
        client.getClientGrantDefaultUserAuthorities().size(); // eagerly load the association
        ClientGrantDefaultUserAuthorities.addAll(client.getClientGrantDefaultUserAuthorities());
        return ClientGrantDefaultUserAuthorities;
    }

    @Transactional(readOnly=true)
	public Page<User> getUsersHaveAuthoritiesUnderMyAuthorities(
			String[] authorityNames, String login, String email, String userName, String mobilePhoneNumber, boolean includeSub, boolean activeOnly,
			Integer offset, Integer limit) {
    	
    	Set<Authority> authoritiesSet = new HashSet<Authority>();
    	for (String authorityName : authorityNames) {
    		authoritiesSet.addAll(getAuthoritiesUnderMyAuthorities(authorityName, includeSub));
		}

    	List<String> authorityNamesList = new ArrayList<String>();
    	authorityNamesList.add("EMPTYLESS");
    	for (Authority authority : authoritiesSet) {
    		authorityNamesList.add(authority.getName());
		} 	
    	
		Page<User> usersPage = userRepository.findAllByActivatedAndAuthorityNameIn(activeOnly, 
				authorityNamesList, login, email, userName, mobilePhoneNumber, PaginationUtil.generatePageRequest(offset, limit));
		for (User user : usersPage) {
			Set<Authority> authoritiesFiltered = new HashSet<Authority>();
			for (Authority authority : user.getAuthorities()) {		
				if(isValidNameUnderMyGrantedAuthorities(authority.getName())) {
					authoritiesFiltered.add(authority);
				}
			}
			user.setAuthorities(authoritiesFiltered);
		}
		return usersPage;
	}   

    @Transactional(readOnly=true)
	public boolean isInClientScope(String authorityName) {
		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		Set<String> scopeSet = new HashSet<String>();
		if(currentAuth instanceof OAuth2Authentication) {
			OAuth2Authentication oAuth2Auth = (OAuth2Authentication) currentAuth;
			scopeSet.addAll(oAuth2Auth.getOAuth2Request().getScope());   	
		}
		
		boolean isInScope = false;
		for (String scope : scopeSet) {
			if(authorityName.equals(scope)
					|| authorityName.startsWith(scope + "_")) {
				isInScope = true;
			}
		}
		return isInScope;
	}  
}
