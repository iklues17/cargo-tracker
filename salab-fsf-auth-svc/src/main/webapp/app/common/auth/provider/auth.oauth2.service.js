'use strict';

angular.module('sampleAdminWebApp')
    .factory('AuthServerProvider', function loginService($location, $http, localStorageService, Base64, 
    		AUTHORIZATION_SERVER_AUTHORIZE_URL, AUTHORIZATION_SERVER_OAUTH_TOKEN_URL, AUTHORIZATION_SERVER_CHECK_TOKEN_URL,
    		LOGOUT_SERVICE_URL, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET) {
        return {
            login: function(credentials) {
            	
            	if(null != localStorageService.get('token')) {
            		localStorageService.remove('token');
            	}
            	
            	switch(credentials.grant_type || $location.search().grant_type) {
        			case "implicit":              
	                    var formElements = angular.element('form[role="form"]'); 
	                    formElements.append('<input type="hidden" name="response_type" value="">');
	                    formElements.append('<input type="hidden" name="client_id" value="">');
	                    formElements.append('<input type="hidden" name="redirect_uri" value="">');
	                    //console.log(formElements);
	                    
	                    formElements[0].action = AUTHORIZATION_SERVER_AUTHORIZE_URL;
	                    formElements[0].method = "POST";
	                    formElements[0].response_type.value = "token";
	                    formElements[0].client_id.value = OAUTH_CLIENT_ID;
	                    formElements[0].redirect_uri.value = $location.search().redirect_uri || location.pathname + "oauth_redirect_token.html";
	                    	
	                    //console.log(formElements[0]);
	                    formElements[0].submit();
	                    /*
                    	var data = "response_type=" + "token" 
	                    	+ "&client_id=" + OAUTH_CLIENT_ID
	                    	+ "&redirect_uri=" + $location.search().redirect_uri
	                    	;	                    
	                    return $http.get(AUTHORIZATION_SERVER_AUTHORIZE_URL + "?" + data, {
	                        headers: {
	                            "Content-Type": "application/x-www-form-urlencoded",
	                            "Authorization": "Basic " + Base64.encode(credentials.username + ':' + credentials.password)
	                        }
	                    }).success(function (response) {
	                        return response;
	                    });
	                    */
	                    break;            	
            		case "authorization_code":
	                    var formElements = angular.element('form[role="form"]'); 
	                    formElements.append('<input type="hidden" name="response_type" value="">');
	                    formElements.append('<input type="hidden" name="client_id" value="">');
	                    formElements.append('<input type="hidden" name="redirect_uri" value="">');
	                    //console.log(formElements);
	                    
	                    formElements[0].action = AUTHORIZATION_SERVER_AUTHORIZE_URL;
	                    formElements[0].method = "POST";
	                    formElements[0].response_type.value = "code";
	                    formElements[0].client_id.value = OAUTH_CLIENT_ID;
	                    formElements[0].redirect_uri.value = $location.search().redirect_uri || location.pathname + "oauth_redirect_code.html";
	                    	
	                    //console.log(formElements[0]);
	                    formElements[0].submit();            			
            			/*
                        var data = "response_type=" + "code" 
                        	+ "&client_id=" + OAUTH_CLIENT_ID
	                    	+ "&redirect_uri=" + $location.search().redirect_uri
	                    	;
	                    return $http.get(AUTHORIZATION_SERVER_AUTHORIZE_URL + "?" + data, {
	                        headers: {
	                            "Content-Type": "application/x-www-form-urlencoded",
	                            "Authorization": "Basic " + Base64.encode(credentials.username + ':' + credentials.password)
	                        }
	                    }).success(function (response) {
	                        return response;
	                    });
	                    */
	                    break;
            		case "client_credentials":
                        var data = "grant_type=client_credentials"
	                    	+ "&client_id=" + OAUTH_CLIENT_ID;
	                    return $http.post(AUTHORIZATION_SERVER_OAUTH_TOKEN_URL, data, {
	                        headers: {
	                            "Content-Type": "application/x-www-form-urlencoded",
	                            "Accept": "application/json",
	                            "Authorization": "Basic " + Base64.encode(OAUTH_CLIENT_ID + ':' + OAUTH_CLIENT_SECRET)
	                        }
	                    }).success(function (response) {
	                        var expiredAt = new Date();
	                        expiredAt.setSeconds(expiredAt.getSeconds() + response.expires_in);
	                        response.expires_at = expiredAt.getTime();
	                        localStorageService.set('token', response);
	                        return response;
	                    });
	                    break;
            		default:
                        var data = "username=" + credentials.username 
	                    	+ "&password=" + credentials.password 
	                    	+ "&grant_type=password"
	                    	+ "&client_secret=" + OAUTH_CLIENT_SECRET
	                    	+ "&client_id=" + OAUTH_CLIENT_ID;
	                    return $http.post(AUTHORIZATION_SERVER_OAUTH_TOKEN_URL, data, {
	                        headers: {
	                            "Content-Type": "application/x-www-form-urlencoded",
	                            "Accept": "application/json",
	                            "Authorization": "Basic " + Base64.encode(OAUTH_CLIENT_ID + ':' + OAUTH_CLIENT_SECRET)
	                        }
	                    }).success(function (response) {
	                        var expiredAt = new Date();
	                        expiredAt.setSeconds(expiredAt.getSeconds() + response.expires_in);
	                        response.expires_at = expiredAt.getTime();
	                        localStorageService.set('token', response);
	                        return response;
	                    });
            	}           		
            },
            logout: function() {
                // logout from the server
                return $http.post(LOGOUT_SERVICE_URL).finally(function() {
                    localStorageService.clearAll();
                });
            },
            getToken: function () {
                return localStorageService.get('token');
            },
            hasValidToken: function () {
                var token = this.getToken();
                return token && token.expires_at && token.expires_at > new Date().getTime();
            },
            checkToken: function() {
            	var data = "token=" + localStorageService.get('token').access_token;
                return $http.post(AUTHORIZATION_SERVER_CHECK_TOKEN_URL, data, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                        "Accept": "application/json",
                        "Authorization": "Basic " + Base64.encode(OAUTH_CLIENT_ID + ':' + OAUTH_CLIENT_SECRET)
                    }
                });
            },
        };
    });
