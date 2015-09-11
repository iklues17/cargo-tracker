'use strict';

angular.module('sampleAdminWebApp.sampleApp')
    .factory('Authorities', function ($resource, AUTHORITIES_SERVICE_URL) {

    	return $resource(AUTHORITIES_SERVICE_URL, {}, {
    			'getGrantableByMe': {
    				method:'GET', 
    				url:AUTHORITIES_SERVICE_URL+'/grantableByMe', 
    				isArray:true, 
    				},
    			'getGrantableByMeGrantTo': {
    				method:'GET', 
    				url:AUTHORITIES_SERVICE_URL+'/grantableByMe/grantTo/:id', 
    				isArray:true, 
    				},
    			'setGrantableByMeGrantTo': {
    				method:'POST', 
    				url:AUTHORITIES_SERVICE_URL+'/grantableByMe/grantTo/:id', 
    				},
    			'getAsClientGrantDefaultUserAuthoritiesTo': {
    				method:'GET', 
    				url:AUTHORITIES_SERVICE_URL+'/grantableByMe/asClientGrantDefaultUserAuthoritiesTo/:clientId', 
    				isArray:true, 
    				},
    			'setAsClientGrantDefaultUserAuthoritiesTo': {
    				method:'POST', 
    				url:AUTHORITIES_SERVICE_URL+'/grantableByMe/asClientGrantDefaultUserAuthoritiesTo/:clientId',
    				},    			
			});
    })
	.factory('Users', function ($resource, USERS_SERVICE_URL) {
		return $resource(USERS_SERVICE_URL, {}, {
			});
	})
	
	;
