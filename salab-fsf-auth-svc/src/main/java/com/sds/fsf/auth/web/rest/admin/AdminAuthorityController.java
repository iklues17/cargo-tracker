package com.sds.fsf.auth.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.sds.fsf.auth.domain.Authority;
import com.sds.fsf.auth.domain.Client;
import com.sds.fsf.auth.repository.AuthorityRepository;
import com.sds.fsf.auth.repository.ClientRepository;
import com.sds.fsf.auth.service.admin.AdminAuthorityService;
import com.wordnik.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/admin")
public class AdminAuthorityController {  
    
	private final Logger log = LoggerFactory.getLogger(AdminAuthorityController.class);

    @Inject
    private AuthorityRepository authorityRepository;
    
    @Inject
    private ClientRepository clientRepository;

    @Inject
    private AdminAuthorityService adminAuthorityService;
    
    /**
     * POST  /authorities -> create or update authorities.
     */
    @ApiOperation(hidden=true, value = "ONLY FOR ADMIN MENU")
    @RequestMapping(value = "/authorities",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> createAuthorities(@RequestBody List<Authority> authoritiesToCreate) 
    {
    	Set<Authority> authoritiesToCreateSet = new HashSet<Authority>(authoritiesToCreate);
    	for (Authority authorityToCreate : authoritiesToCreateSet) {
			if(!adminAuthorityService.isValidNameUnderMyGrantedAuthorities(authorityToCreate.getName())) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
    	authorityRepository.save(authoritiesToCreate);
    	return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    /**
     * DELETE  /authorities -> delete authorities.
     */
    @ApiOperation(hidden=true, value = "ONLY FOR ADMIN MENU")
    @RequestMapping(value = "/authorities",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> deleteAuthorities(@RequestParam Map<String, String> params) {
    	String[] authorityNames = params.get("authorities").split(",");
    	for (String authorityName : authorityNames) {
			if(null == authorityName || authorityName.length() == 0 || !adminAuthorityService.isValidNameUnderMyGrantedAuthorities(authorityName)) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} else if(adminAuthorityService.isDeletableAuthority(authorityName)) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}    	
    	for (String authorityName : authorityNames) {
    		if(null != authorityName && authorityName.length() > 0 && adminAuthorityService.isValidNameUnderMyGrantedAuthorities(authorityName)) {
    			authorityRepository.delete(authorityName);
    		}
		}
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /**
     * GET  /authorities -> get all authorities.
     */
    @ApiOperation(hidden=true, value = "ONLY FOR ADMIN MENU")
    @RequestMapping(value = "/authorities",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Collection<Authority>> getAuthorities(@RequestParam(value="userId",required=false) Long userId) {  	
    	if(null != userId) {
    		Set<Authority> grantedAuthoritesSet = adminAuthorityService.getUserAuthorities(userId);
    		return new ResponseEntity<Collection<Authority>>(grantedAuthoritesSet, HttpStatus.OK);
    	}
    	List<Authority> authorities = authorityRepository.findAll();
        return new ResponseEntity<Collection<Authority>>(authorities, HttpStatus.OK);
    }
    
    /**
     * GET  /authorities/grantableByMe -> get authorities grantable by.
     */
    @ApiOperation(hidden=true, value = "ONLY FOR ADMIN MENU")
    @RequestMapping(value = "/authorities/grantableByMe",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Authority>> getAuthoritiesGrantableByMe() {  	

    	Set<Authority> grantableAuthorities = adminAuthorityService.getMyGrantableAuthorities();
        return new ResponseEntity<Set<Authority>>(grantableAuthorities, HttpStatus.OK);
    } 
    
    /**
     * GET  /authorities/grantableByMe/grantTo/{id} -> get authorities granted to an ID among authorities grantable by.
     */
    @ApiOperation(hidden=true, value = "ONLY FOR ADMIN MENU")
    @RequestMapping(value = "/authorities/grantableByMe/grantTo/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Authority>> getAuthoritiesGrantToAnIDAmongAuthoritiesGrantableByMe(@PathVariable("id") Long id) {  	

    	Set<Authority> authorities = adminAuthorityService.getAuthoritiesGrantedToLoginUnderMyAuthorities(id);
    	return new ResponseEntity<Set<Authority>>(authorities, HttpStatus.OK);
    			
    }        
    
    /**
     * POST  /authorities/grantableByMe/grantTo/{id} -> grant authorities to user.
     */
    @ApiOperation(hidden=true, value = "ONLY FOR ADMIN MENU")
    @RequestMapping(value = "/authorities/grantableByMe/grantTo/{id}",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> setAuthoritiesGrantToAnIDAmongAuthoritiesGrantableByMe(@PathVariable("id") Long id, @RequestBody List<Authority> grantedAuthoritiesFromClient) 
    {
		boolean isSucceed = adminAuthorityService.saveUserAuthoritiesUnderMyAuthorities(id, grantedAuthoritiesFromClient, false);    		
		if(!isSucceed) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	return new ResponseEntity<>(HttpStatus.OK);
    }       
    
    /**
     * GET  /authorities/grantableByMe/clientGrantDefaultUserAuthorities/{clientId} -> get authorities as client grant default user client to an client ID among authorities grantable by.
     */
    @ApiOperation(hidden=true, value = "ONLY FOR ADMIN MENU")
    @RequestMapping(value = "/authorities/grantableByMe/asClientGrantDefaultUserAuthoritiesTo/{clientId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Authority>> getAuthoritiesAsClientGrantDefaultUserAuthoritiesToAnClientIdAmongAuthoritiesGrantableByMe(@PathVariable("clientId") String clientId) {  	

    	Set<Authority> grantedAuthoritesSet = adminAuthorityService.getClientGrantDefaultUserAuthorities(clientId);
    	grantedAuthoritesSet.retainAll(adminAuthorityService.getMyGrantableAuthorities());
    	
        return new ResponseEntity<Set<Authority>>(grantedAuthoritesSet, HttpStatus.OK);
    }        
    
    /**
     * POST  /authorities/grantableByMe/asClientGrantDefaultUserAuthoritiesTo/{clientId} -> set authorities as client grant default user client to an client ID among authorities grantable by.
     */
    @ApiOperation(hidden=true, value = "ONLY FOR ADMIN MENU")
    @RequestMapping(value = "/authorities/grantableByMe/asClientGrantDefaultUserAuthoritiesTo/{clientId}",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> setAuthoritiesAsClientGrantDefaultUserAuthoritiesToAnClientIdAmongAuthoritiesGrantableBy(@PathVariable("clientId") String clientId, @RequestBody List<Authority> configerdAuthoritiesFromClient) 
    {
    	Set<Authority> configuredAuthoritiesFromDB = adminAuthorityService.getClientGrantDefaultUserAuthorities(clientId);
    	
    	Set<Authority> authoritiesToBeRemoved = adminAuthorityService.getMyGrantableAuthorities();
    	authoritiesToBeRemoved.removeAll(configerdAuthoritiesFromClient);
    	configuredAuthoritiesFromDB.removeAll(authoritiesToBeRemoved);
    	
    	configerdAuthoritiesFromClient.retainAll(adminAuthorityService.getMyGrantableAuthorities());
    	configuredAuthoritiesFromDB.addAll(configerdAuthoritiesFromClient);
    	
    	Client client = clientRepository.findOne(clientId);
    	client.setClientGrantDefaultUserAuthorities(configuredAuthoritiesFromDB);
    	clientRepository.save(client);
    	return new ResponseEntity<>(HttpStatus.OK);
    }     
}
