package com.sds.fsf.auth.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sds.fsf.cmm.security.util.SecurityUtils;
import com.sds.fsf.auth.domain.Authority;
import com.sds.fsf.auth.domain.User;
import com.sds.fsf.auth.repository.UserRepository;
import com.sds.fsf.auth.service.AccountService;
import com.sds.fsf.auth.service.AuthorityService;
import com.sds.fsf.auth.service.UserService;
import com.sds.fsf.auth.web.rest.dto.OnetimeLinkResponseDTO;
import com.sds.fsf.auth.web.rest.dto.RandomizePasswordRequestDTO;
import com.sds.fsf.auth.web.rest.dto.RandomizePasswordResponseDTO;
import com.sds.fsf.auth.web.rest.dto.UserIdRequestDTO;
import com.sds.fsf.auth.web.rest.dto.OnetimeLinkRequestDTO;
import com.sds.fsf.auth.web.rest.dto.UserDTOIncludeRoles;
import com.sds.fsf.auth.web.rest.dto.UserDTO;
import com.sds.fsf.auth.web.rest.dto.UserIdResponseDTO;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;
    
    @Inject
    private AuthorityService authorityService;    
    
    @Inject
    private AccountService accountService;

   // @Inject
   // private MailService mailService;

    /**
     * POST  /register -> register the user.
     */
    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> registerAccount(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
        User user = userRepository.findOneByLogin(userDTO.getLogin());
        if (user != null) {
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("login already in use");
        } else {
            if (userRepository.findOneByEmail(userDTO.getEmail()) != null) {
                return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("e-mail address already in use");
            }
            user = userService.createUserInformation(
            		userDTO.getLogin(), 
            		userDTO.getPassword(),
		            userDTO.getFirstName(), 
		            userDTO.getLastName(), 
		            userDTO.getEmail().toLowerCase(),
		            userDTO.getMobilePhoneNumber(),
		            userDTO.getLangKey());
//            String baseUrl = request.getScheme() + // "http"
//            "://" +                            // "://"
//            request.getServerName() +          // "myhost"
//            ":" +                              // ":"
//            request.getServerPort();           // "80"
//            mailService.sendActivationEmail(user, baseUrl);
            
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
    /**
     * GET  /activate -> activate the registered user.
     */
    @RequestMapping(value = "/activate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
        User user = userService.activateRegistration(key);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /**
     * GET  /deactivate -> deactivate the registered user.
     */
    @RequestMapping(value = "/deactivate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> deactivateAccount() {
        String currentLogin = SecurityUtils.getCurrentLogin();
        if (null == currentLogin) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User currentUser = userRepository.findOneByLogin(currentLogin);
        if(null == currentUser) {
        	return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        currentUser.setActivated(false);
        userRepository.save(currentUser);
        return new ResponseEntity<String>(HttpStatus.OK);
    }    
    
    /**
     * GET  /authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/authenticate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account -> get the current user.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserDTOIncludeRoles> getAccount() {
        User user = userService.getUserWithAuthorities();
        List<String> roles = new ArrayList<>();
        if (user == null) {
        	if(SecurityUtils.isClientAuthenticated()) {
        		return new ResponseEntity<UserDTOIncludeRoles>(
        				new UserDTOIncludeRoles(null,null,null,null,null,null,null,SecurityUtils.getAuthorities(), null),
        				HttpStatus.OK
        				);
        	} else {
        		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        }
        
        for (Authority authority : user.getAuthorities()) {
        	if(SecurityUtils.isUserInRole(authority.getName())
        	&& authorityService.isInClientScope(authority.getName())) {
        		roles.add(authority.getName());
        	}
        }
        
        Map<String, Object> infos = new HashMap<String, Object>();
        if(user.getPasswordExpiredDate() != null && user.getPasswordExpiredDate().compareTo(DateTime.now()) <= 0) {
        	infos.put("passwordExpired", Boolean.TRUE);
        } else {
        	infos.put("passwordExpired", Boolean.FALSE);
        }
        
        return new ResponseEntity<>(
            new UserDTOIncludeRoles(
                user.getLogin(),
                null,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getMobilePhoneNumber(),
                user.getLangKey(),
                roles,
                infos
                ),
            HttpStatus.OK);
    }

    /**
     * POST  /account -> update the current user information.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> saveAccount(@RequestBody UserDTO userDTO) {
        User userHavingThisLogin = userRepository.findOneByLogin(userDTO.getLogin());
        if (userHavingThisLogin != null && !userHavingThisLogin.getLogin().equals(SecurityUtils.getCurrentLogin())) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        userService.updateUserInformation(
        		userDTO.getFirstName(), 
        		userDTO.getLastName(), 
        		userDTO.getEmail(), 
        		userDTO.getMobilePhoneNumber(), 
        		userDTO.getLangKey());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * DELETE  /account -> delete the current user.

    @RequestMapping(value = "/account",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> deleteAccount() {
        String currentLogin = SecurityUtils.getCurrentLogin();
        if (null == currentLogin) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User currentUser = userRepository.findOneByLogin(currentLogin);
        if(null == currentUser) {
        	return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        userRepository.delete(currentUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }    
      */   
    
    /**
     * POST  /change_password -> changes the current user's password
     */
    @RequestMapping(value = "/account/change_password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> changePassword(@RequestBody String password) {
        if (StringUtils.isEmpty(password)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }
 
    /**
     * POST  /send_onetime_link_by_email_to_reset_password
     */
    @RequestMapping(value = "/send_onetime_link_by_email_to_reset_password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> sendOnetimeLinkByEmailToResetPassword(@RequestBody OnetimeLinkRequestDTO onetimeLinkRequestDTO) {
    	String login = onetimeLinkRequestDTO.getLogin();
    	String email = onetimeLinkRequestDTO.getEmail();
    	if(StringUtils.isEmpty(login) || StringUtils.isEmpty(email)) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	User user = userRepository.findOneByLogin(login);
    	if(null == user || !email.equals(user.getEmail())){
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	//Send Onetime Link By Email
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST  /send_login_by_email
     */
    @RequestMapping(value = "/send_login_by_email",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> sendLoginByEmail(@RequestBody UserIdRequestDTO userIdRequestDTO) {
    	String email = userIdRequestDTO.getEmail();
    	if(StringUtils.isEmpty(email)) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	User user = userRepository.findOneByEmail(email);
    	if(null == user || !email.equals(user.getEmail())){
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	//Send Onetime Link By Email
        return new ResponseEntity<>(HttpStatus.OK);
    }    

    /**
     * POST  /randomize_password
     */
    @RequestMapping(value = "/randomize_password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RandomizePasswordResponseDTO> getRandomizePassword(@RequestBody RandomizePasswordRequestDTO randomizePasswordDTO) {
    	String login = randomizePasswordDTO.getLogin();
    	String email = randomizePasswordDTO.getEmail();
    	if(StringUtils.isEmpty(login) || StringUtils.isEmpty(email)) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	User user = userRepository.findOneByLogin(login);
    	if(null == user || !email.equals(user.getEmail())){
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	String randomizedPassword = accountService.getRandomizePassword(user.getLogin());
        return new ResponseEntity<RandomizePasswordResponseDTO>(new RandomizePasswordResponseDTO(randomizedPassword), HttpStatus.OK);
    }    

    /**
     * POST  /get_login
     */
    @RequestMapping(value = "/get_login",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserIdResponseDTO> getLogin(@RequestBody UserIdRequestDTO userIdRequestDTO) {
    	String email = userIdRequestDTO.getEmail();
    	if(StringUtils.isEmpty(email)) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	User user = userRepository.findOneByEmail(email);
    	if(null == user || !email.equals(user.getEmail())){
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}

        return new ResponseEntity<UserIdResponseDTO>(new UserIdResponseDTO(user.getLogin()), HttpStatus.OK);
    }    
}
