package com.sds.fsf.auth.service;

import com.sds.fsf.cmm.service.util.RandomUtil;
import com.sds.fsf.cmm.security.util.SecurityUtils;
import com.sds.fsf.auth.domain.Authority;
import com.sds.fsf.auth.domain.User;
import com.sds.fsf.auth.repository.AuthorityRepository;
import com.sds.fsf.auth.repository.UserRepository;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

	private final static long MILLISECOND_PASSWORD_EXPIRED_IN = 1000L*60L*60L*24L*90L;
	
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;
    
    @Inject
    private AuthorityService authorityService;

    public  User activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        User user = userRepository.findOneByActivationKey(key);
        // activate given user for the registration key.
        if (user != null) {
            user.setActivated(true);
            user.setActivationKey(null);
            userRepository.save(user);
            log.debug("Activated user: {}", user);
        }
        return user;
    }

    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
                                      String mobilePhoneNumber, String langKey, boolean isActivated, String authorityBase) {
        User newUser = new User();
        
        newUser.setLogin(login);
        
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encryptedPassword);
        
        newUser.setFirstName(firstName);
        
        newUser.setLastName(lastName);
        
        newUser.setEmail(email);
        
        newUser.setMobilePhoneNumber(mobilePhoneNumber);
        
        newUser.setLangKey(langKey);
        
        newUser.setActivated(isActivated);
        if(!isActivated) {
        	newUser.setActivationKey(RandomUtil.generateActivationKey());
        }
        
        Set<Authority> authorities = new HashSet<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof OAuth2Authentication) {
        	String clientId = ((OAuth2Authentication)authentication).getOAuth2Request().getClientId();
        	authorities.addAll(authorityService.getClientGrantDefaultUserAuthorities(clientId));
        } 
        Authority defaultAuthority = authorityRepository.findOne("ROLE_USER");
        authorities.add(defaultAuthority);
        newUser.setAuthorities(authorities);
        
        if(!StringUtils.isEmpty(authorityBase)
        && (SecurityUtils.isUserInRole(authorityBase) || authorityService.isValidNameUnderMyGrantedAuthorities(authorityBase))
        && authorityRepository.findOne(authorityBase) != null
        ){
        	newUser.setAuthorityBase(authorityBase);
        } else {
        	newUser.setAuthorityBase("ROLE");
        }  
        Set<Authority> filteredAuthorities = authorityService.authorityBaseFilter(newUser.getAuthorityBase(), authorities);
        newUser.setAuthorities(filteredAuthorities);
        
        newUser.setPasswordUpdatedDate(DateTime.now());
        //After 90 days, password'll be expired.
        newUser.setPasswordExpiredDate(new DateTime(DateTime.now().getMillis() + MILLISECOND_PASSWORD_EXPIRED_IN));
        
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
            String mobilePhoneNumber, String langKey) {
    	return createUserInformation(login, password, firstName, lastName, email, 
    			StringUtils.isEmpty(mobilePhoneNumber) ? null : mobilePhoneNumber, 
    			langKey, true, null);
    }

    public void updateUserInformation(String firstName, String lastName, String email, String mobilePhoneNumber, String langKey) {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setEmail(email);
        currentUser.setMobilePhoneNumber(StringUtils.isEmpty(mobilePhoneNumber) ? null : mobilePhoneNumber);
        if(!StringUtils.isEmpty(langKey.trim())) {
        	currentUser.setLangKey(langKey);	
        }
        userRepository.save(currentUser);
        log.debug("Changed Information for User: {}", currentUser);
    }
    
    public boolean checkCurrentPasswordAndUpdateUserInformation(
    		String password, String passwordNew,
    		String firstName, String lastName, 
    		String email, String mobilePhoneNumber
    		) {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(StringUtils.isEmpty(passwordNew)) {
        	passwordNew = password;
        }        
        if(!checkCurrentPasswordAndChangePassword(password, passwordNew)) {
        	return false;
        }
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setEmail(email);
        currentUser.setMobilePhoneNumber(mobilePhoneNumber);
        userRepository.save(currentUser);
        log.debug("Changed Information for User: {}", currentUser);
        return true;
    }    

    public void changePassword(String password) {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        String encryptedPassword = passwordEncoder.encode(password);
        currentUser.setPassword(encryptedPassword);
        currentUser.setPasswordUpdatedDate(DateTime.now());
        //After 90 days, password'll be expired.
        currentUser.setPasswordExpiredDate(new DateTime(DateTime.now().getMillis() + MILLISECOND_PASSWORD_EXPIRED_IN));
        userRepository.save(currentUser);
        log.debug("Changed password for User: {}", currentUser);
    }

    public boolean checkCurrentPasswordAndChangePassword(String password, String passwordNew) {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());     
        if(!passwordEncoder.matches(password, currentUser.getPassword())) {
        	log.debug("Current password not matched from db for User: {}", currentUser);
        	return false;
        }
        changePassword(passwordNew);
        return true;
    }
    

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        if(null != currentUser) currentUser.getAuthorities().size(); // eagerly load the association
        return currentUser;
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        DateTime now = new DateTime();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
        }
    }
}
