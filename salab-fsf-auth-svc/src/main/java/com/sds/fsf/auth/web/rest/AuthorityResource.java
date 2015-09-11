package com.sds.fsf.auth.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sds.fsf.cmm.web.rest.util.PaginationUtil;
import com.sds.fsf.auth.domain.Authority;
import com.sds.fsf.auth.domain.User;
import com.sds.fsf.auth.service.AuthorityService;
import com.sds.fsf.auth.web.rest.dto.GrantAuthoritiesDTO;
import com.sds.fsf.auth.web.rest.dto.UserDTOIncludeRoles;
import com.wordnik.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.util.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AuthorityResource {

	private final Logger log = LoggerFactory.getLogger(AuthorityResource.class);
    
    @Inject
    private AuthorityService authorityService;    

    /**
     * GET  /authorities/granted -> get granted authorities.
     */
    @RequestMapping(value = "/authorities/granted/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Authority>> getAuthoritiesGrantedToLogin(@PathVariable("login") String login) 
    {
    	Set<Authority> authorities = authorityService.getAuthoritiesGrantedToLoginUnderMyAuthorities(login);
    	return new ResponseEntity<Set<Authority>>(authorities, HttpStatus.OK);
    }
  
    /**
     * GET  /authorities/granted -> get granted authorities.
     */
    @RequestMapping(value = "/authorities/granted_to_me",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Authority>> getAuthoritiesGrantedToMe() 
    {
    	Set<Authority> authorities = authorityService.getMyGrantedAuthorities();
    	return new ResponseEntity<Set<Authority>>(authorities, HttpStatus.OK);
    }     

    /**
     * GET  /authorities/grantable -> get grantable authorities.
     */
    @RequestMapping(value = "/authorities/grantable",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Authority>> getMyGrantableAuthorities() 
    {
    	Set<Authority> authorities = authorityService.getMyGrantableAuthorities();
    	return new ResponseEntity<Set<Authority>>(authorities, HttpStatus.OK);
    }       

    /**
     * GET  /authorities/{authorityName} -> get authorities.
     */
    @RequestMapping(value = "/authorities/under/{authorityName}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Authority>> getMyAuthorities(@PathVariable("authorityName") String authorityName, @RequestParam(value="includeSub", defaultValue="false") boolean includeSub) 
    {
    	Set<Authority> authorities = authorityService.getAuthoritiesUnderMyAuthorities(authorityName, includeSub);
    	return new ResponseEntity<Set<Authority>>(authorities, HttpStatus.OK);
    }     
    
    /**
     * GET  /authorities/{authorityName}/users -> get users that have some authorities.
     */
    @RequestMapping(value = "/authorities/under/{authorityName}/users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Map<String, Object>>> getUsersHaveAuthorities(
    		@PathVariable("authorityName") String authorityName,
    		@RequestParam(value="includeSub", defaultValue="false") boolean includeSub,
    		@RequestParam(value="activeOnly", defaultValue="false") boolean activeOnly
    	){ 
    	ArrayList<Map<String, Object>> authoritiesAndUsersMapList = authorityService.getUsersHaveAuthoritiesUnderMyAuthorities(
				authorityName, includeSub, activeOnly);    	
    	return new ResponseEntity<List<Map<String, Object>>>(authoritiesAndUsersMapList, HttpStatus.OK);
    }

    /**
     * GET  /authorities/search?findUsers -> find users with some conditions
     */
    @RequestMapping(value = "/authorities/search",
    		params = "q=findUsers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserDTOIncludeRoles>> findUsers(
    		@RequestParam(value = "includeSub", defaultValue="false") boolean includeSub,
    		@RequestParam(value = "activeOnly", defaultValue="false") boolean activeOnly,
    		@RequestParam(value = "authorityName") String[] authorityNames,
    		@RequestParam(value = "login", defaultValue="") String login,
    		@RequestParam(value = "email", defaultValue="") String email,
    		@RequestParam(value = "userName", defaultValue="") String userName,
    		@RequestParam(value = "mobilePhoneNumber", defaultValue="") String mobilePhoneNumber,
    		@RequestParam(value = "page" , required = false) Integer offset,
            @RequestParam(value = "per_page", required = false) Integer limit
    	){ 
    	Page<User> usersPage = authorityService.getUsersHaveAuthoritiesUnderMyAuthorities(authorityNames, login, email, userName, mobilePhoneNumber, includeSub, activeOnly, offset, limit);
    	List<UserDTOIncludeRoles> userDTOsList = new  ArrayList<UserDTOIncludeRoles>();
    	for (User user : usersPage) {
			UserDTOIncludeRoles userDTO = new UserDTOIncludeRoles(
					user.getLogin(),
					null,
					user.getFirstName(),
					user.getLastName(),
					user.getEmail(),
					user.getMobilePhoneNumber(),
					user.getLangKey(),
					new ArrayList<String>(),
					null
					);
			for (Authority authority : user.getAuthorities()) {
				userDTO.getRoles().add(authority.getName());
			}
			userDTOsList.add(userDTO);
		}  	
    	HttpHeaders headers = PaginationUtil.setTotalElements(usersPage);
    	return new ResponseEntity<List<UserDTOIncludeRoles>>(userDTOsList, headers, HttpStatus.OK);
    	
    }
    
    
    /**
     * POST  /authorities/grant/{login} -> grant authorities to user.
     */
    @RequestMapping(value = "/authorities/grant/{login}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> grantAuthoritiesTo(@PathVariable("login") String login, 
    		@RequestParam(value="noRoleDeactived", defaultValue="false") boolean noRoleDeactived,
    		@ApiParam(value=""
    				+ "ex)<br>"
    				+ "<i>[<br>"
    				+ "&nbsp;&nbsp;{\"name\":\"ROLE_A\"},<br>"
    				+ "&nbsp;&nbsp;{\"name\":\"ROLE_B\"}<br>"
    				+ "]</i>") @RequestBody List<Authority> grantedAuthoritiesFromClient
    	){
		boolean isSucceed = authorityService.saveUserAuthoritiesUnderMyAuthorities(login, grantedAuthoritiesFromClient, noRoleDeactived);    		
		if(!isSucceed) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * POST  /authorities/grant -> grant authorities to users.
     */
    @RequestMapping(value = "/authorities/grant",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> grantAuthoritiesTo(
    		@RequestParam(value="noRoleDeactived", defaultValue="false") boolean noRoleDeactived,
    		@ApiParam(value=""
    				+ "ex)<br>"
    				+ "[&nbsp;{<br>"
    				+ "&nbsp;&nbsp;\"login\":\"userA\",<br>"
    				+ "&nbsp;&nbsp;\"roles\":[<br>"
    				+ "&nbsp;&nbsp;&nbsp;\"ROLE_A\",<br>"
    				+ "&nbsp;&nbsp;&nbsp;\"ROLE_B\"<br>"
    				+ "&nbsp;&nbsp;]&nbsp;},&nbsp;{<br>"
    				+ "&nbsp;&nbsp;\"login\":\"userB\",<br>"
    				+ "&nbsp;&nbsp;\"roles\":[<br>"
    				+ "&nbsp;&nbsp;&nbsp;\"ROLE_C\",<br>"
    				+ "&nbsp;&nbsp;&nbsp;\"ROLE_D\"<br>"
    				+ "]&nbsp;}&nbsp;]<br>") @RequestBody List<GrantAuthoritiesDTO> grantAuthoritiesDTOs
    	){
    	String login;
    	List<Authority> grantedAuthoritiesFromClient;
    	for (GrantAuthoritiesDTO grantAuthoritiesDTO : grantAuthoritiesDTOs) {
    		login = grantAuthoritiesDTO.getLogin();
    		grantedAuthoritiesFromClient = new ArrayList<Authority>();
    		List<String> roles = grantAuthoritiesDTO.getRoles();
    		for (String role : roles) {
    			Authority authority = new Authority();
    			authority.setName(role);
    			grantedAuthoritiesFromClient.add(authority);
			}

			boolean isSucceed = authorityService.saveUserAuthoritiesUnderMyAuthorities(login, grantedAuthoritiesFromClient, noRoleDeactived);    		
			if(!isSucceed) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
    	
    	return new ResponseEntity<>(HttpStatus.OK);
    }   
}
