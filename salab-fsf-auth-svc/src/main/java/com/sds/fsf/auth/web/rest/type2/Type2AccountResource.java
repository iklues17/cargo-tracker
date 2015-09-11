package com.sds.fsf.auth.web.rest.type2;

import com.codahale.metrics.annotation.Timed;
import com.sds.fsf.cmm.security.util.SecurityUtils;
import com.sds.fsf.auth.domain.Authority;
import com.sds.fsf.auth.domain.User;
import com.sds.fsf.auth.repository.UserRepository;
import com.sds.fsf.auth.service.AuthorityService;
import com.sds.fsf.auth.service.UserService;
import com.sds.fsf.auth.web.rest.type2.dto.Type2GeneralResultResponseDTO;
import com.sds.fsf.auth.web.rest.type2.dto.Type2PasswordDTO;
import com.sds.fsf.auth.web.rest.type2.dto.Type2UserDTOForResponse;
import com.sds.fsf.auth.web.rest.type2.dto.Type2UserDTOForRegister;
import com.sds.fsf.auth.web.rest.type2.dto.Type2UserDTOForResponseActivationKey;
import com.sds.fsf.auth.web.rest.type2.dto.Type2UserDTOForUpdate;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

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
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/t2")
public class Type2AccountResource {

    private final Logger log = LoggerFactory.getLogger(Type2AccountResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private AuthorityService authorityService;
    
   // @Inject
   // private MailService mailService;

    /**
     * POST  /register -> register the user.
     */
    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
	@ApiOperation(response = Type2UserDTOForResponseActivationKey.class, value = "")
    public ResponseEntity<?> registerAccount(@Valid @RequestBody Type2UserDTOForRegister userDTO, HttpServletRequest request) {
        User user = userRepository.findOneByLogin(userDTO.getLogin());
        if (user != null) {
        	Type2GeneralResultResponseDTO result = new Type2GeneralResultResponseDTO("failure", "login already in use");
            return ResponseEntity.badRequest().body(result);
        } else {
            if (userRepository.findOneByEmail(userDTO.getEmail()) != null) {
            	Type2GeneralResultResponseDTO result = new Type2GeneralResultResponseDTO("failure", "e-mail address already in use");
                return ResponseEntity.badRequest().body(result);
            }
            user = userService.createUserInformation(
            		userDTO.getLogin(), 
            		userDTO.getPassword(),
            		userDTO.getFirstName(), 
            		userDTO.getLastName(), 
            		userDTO.getEmail().toLowerCase(), 
            		userDTO.getMobilePhoneNumber(),
            		userDTO.getLangKey(),
            		false,
            		userDTO.getAuthorityBase()
            	);
//            String baseUrl = request.getScheme() + // "http"
//            "://" +                            // "://"
//            request.getServerName() +          // "myhost"
//            ":" +                              // ":"
//            request.getServerPort();           // "80"
//            mailService.sendActivationEmail(user, baseUrl);
            
            List<String> roles = new ArrayList<>();
            for (Authority authority : user.getAuthorities()) {
            	if(SecurityUtils.isUserInRole(authority.getName())) {
            		roles.add(authority.getName());
            	}
            }
            Type2UserDTOForResponseActivationKey responseUserDTO = new Type2UserDTOForResponseActivationKey(
            		user.getLogin(),
            		null,
            		user.getFirstName(), 
            		user.getLastName(),
            		user.getEmail(),
            		user.getMobilePhoneNumber(),
            		user.getLangKey(), 
            		user.getAuthorityBase(),
            		roles,
            		null,
            		user.getActivated(),
            		user.getActivationKey());
            return new ResponseEntity<Type2UserDTOForResponseActivationKey>(responseUserDTO, HttpStatus.CREATED);
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
    public ResponseEntity<Type2UserDTOForResponse> getAccount() {
        User user = userService.getUserWithAuthorities();
        List<String> roles = new ArrayList<>();
        if (user == null) {
        	if(SecurityUtils.isClientAuthenticated()) {
        		return new ResponseEntity<Type2UserDTOForResponse>(
        				new Type2UserDTOForResponse(null,null,null,null,null,null,null,null,SecurityUtils.getAuthorities(),null),
        				HttpStatus.OK
        				);
        	} else {
        		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        }
        for (Authority authority : user.getAuthorities()) {
        	if(SecurityUtils.isUserInRole(authority.getName())
        	&& authorityService.isInClientScope(authority.getName())){
        		roles.add(authority.getName());
        	}
        }
        
        Map<String, String> infos = new HashMap<String, String>();
        if(user.getPasswordExpiredDate() != null && user.getPasswordExpiredDate().compareTo(DateTime.now()) <= 0) {
        	infos.put("passwordExpired", Boolean.TRUE.toString());
        } else {
        	infos.put("passwordExpired", Boolean.FALSE.toString());
        }        
        return new ResponseEntity<Type2UserDTOForResponse>(
            new Type2UserDTOForResponse(
                user.getLogin(),
                null,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getMobilePhoneNumber(),
                user.getLangKey(),
                user.getAuthorityBase(),
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
    public ResponseEntity<String> saveAccount(@RequestBody Type2UserDTOForUpdate userDTO) {
    	User userHavingThisLogin = userRepository.findOneByLogin(userDTO.getLogin());
        if (userHavingThisLogin != null && !userHavingThisLogin.getLogin().equals(SecurityUtils.getCurrentLogin())) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }	
		boolean isUpdated = userService.checkCurrentPasswordAndUpdateUserInformation(
				userDTO.getPassword(), userDTO.getPasswordNew(),
				userDTO.getFirstName(), userDTO.getLastName(), 
				userDTO.getEmail(), userDTO.getMobilePhoneNumber()
				);
		if(isUpdated) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    /**
     * POST  /change_password -> changes the current user's password
     */
    @RequestMapping(value = "/account/change_password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> changePassword(@RequestBody Type2PasswordDTO passwordDTO) {
    	String password = passwordDTO.getPassword();
    	String passwordNew = passwordDTO.getPasswordNew();
    	
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(passwordNew)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        boolean isChanged = userService.checkCurrentPasswordAndChangePassword(password, passwordNew);
        if(!isChanged) {
        	return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * POST  /check_validation/email -> check email validation.
     */
    @RequestMapping(value = "/register/validate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Type2GeneralResultResponseDTO> checkValidation(
    		@ApiParam(name = "type", value = "\"login\" or \"email\"") @RequestParam(value = "type") String type, 
    		@RequestParam("value") String value) {
    	log.debug("check {} validation : {}", type, value);
    	
    	Type2GeneralResultResponseDTO result;
    	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    	Set<ConstraintViolation<Type2UserDTOForRegister>> constraintViolations 
    		= validator.validateValue(Type2UserDTOForRegister.class, type, value);
    	if(constraintViolations.size() > 0) {
    		result = new Type2GeneralResultResponseDTO("invalid", constraintViolations.iterator().next().getMessage());
    		return new ResponseEntity<Type2GeneralResultResponseDTO>(result, HttpStatus.BAD_REQUEST);
    	}
    	
        if (type.equals("email") && userRepository.findOneByEmail(value) != null) {
        	result = new Type2GeneralResultResponseDTO("invalid", "e-mail address already in use");
    		return new ResponseEntity<Type2GeneralResultResponseDTO>(result, HttpStatus.BAD_REQUEST);
        } else if(type.equals("login") && userRepository.findOneByLogin(value) != null){
        	result = new Type2GeneralResultResponseDTO("invalid", "login already in use");
    		return new ResponseEntity<Type2GeneralResultResponseDTO>(result, HttpStatus.BAD_REQUEST);
        }
        result = new Type2GeneralResultResponseDTO("valid", null);
        return new ResponseEntity<Type2GeneralResultResponseDTO>(result, HttpStatus.OK);
    }
}
