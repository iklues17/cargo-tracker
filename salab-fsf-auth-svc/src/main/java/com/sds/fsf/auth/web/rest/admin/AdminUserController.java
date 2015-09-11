package com.sds.fsf.auth.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.sds.fsf.auth.domain.User;
import com.sds.fsf.auth.repository.UserRepository;
import com.sds.fsf.cmm.web.rest.util.PaginationUtil;
import com.wordnik.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.util.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    private final Logger log = LoggerFactory.getLogger(AdminUserController.class);

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private PasswordEncoder passwordEncoder;
    
    /**
     * POST  /users -> create or update users.
     */
    @ApiOperation(hidden=true, value = "ONLY FOR ADMIN MENU")
    @RequestMapping(value = "/users",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> createUsers(@RequestBody List<User> users) 
    {
    	for (User user : users) {
    		if(user == null
    				|| StringUtils.isEmpty(user.getLogin())
    				|| StringUtils.isEmpty(user.getTransientPassword())
    				) {
    			continue;
    		}
    		
    		User userInDB = null;
    		if(null != user.getId()) {
    			userInDB = userRepository.findOne(user.getId());
    			if(null == userInDB) {
    				userInDB = new User();
    			}	    			
    		} else {
    			userInDB = new User();
    		}
    		userInDB.setLogin(user.getLogin());
    		
			userInDB.setActivated(user.getActivated());
			userInDB.setActivationKey(user.getActivationKey());
			userInDB.setAuthorityBase(user.getAuthorityBase());
			userInDB.setEmail(user.getEmail());
			userInDB.setFirstName(user.getFirstName());
			userInDB.setLastName(user.getLastName());
			userInDB.setLangKey(user.getLangKey());
			userInDB.setLoginFailureCount(user.getLoginFailureCount());
			userInDB.setMobilePhoneNumber(user.getMobilePhoneNumber());
			userInDB.setPasswordExpiredDate(user.getPasswordExpiredDate());
			userInDB.setPasswordUpdatedDate(user.getPasswordUpdatedDate());
			
    		String newPassword = user.getTransientPassword();
    		if(null != newPassword && 0 != newPassword.length()) {
	    		String encrypted = passwordEncoder.encode(newPassword);
	    		userInDB.setPassword(encrypted);
    		}
    		userRepository.save(userInDB);
		}
    	return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    /**
     * DELETE  /users -> delete users.
     */
    @ApiOperation(hidden=true, value = "ONLY FOR ADMIN MENU")
    @RequestMapping(value = "/users",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> deleteUsers(@RequestParam Map<String, String> params) {
    	String[] users = params.get("users").split(",");
    	for (String user : users) {
    		if(null != user && user.length() > 0) {
    			userRepository.delete(Long.parseLong(user));
    		}
		}
        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    /**
     * GET  /users -> get all users.
     */
    @ApiOperation(hidden=true, value = "ONLY FOR ADMIN MENU")
    @RequestMapping(value = "/users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<User>> getAllUsers(
    		@RequestParam(value = "page" , required = false) Integer offset,
            @RequestParam(value = "per_page", required = false) Integer limit
    		) {
    	Page<User> usersPage = userRepository.findAll(PaginationUtil.generatePageRequest(offset, limit, Direction.ASC, "login"));
    	List<User> users = usersPage.getContent();
    	if(users.size() == 0) {
    		users.add(new User());
    	}
        return new ResponseEntity<List<User>>(users, PaginationUtil.setTotalElements(usersPage), HttpStatus.OK);
    }
}
