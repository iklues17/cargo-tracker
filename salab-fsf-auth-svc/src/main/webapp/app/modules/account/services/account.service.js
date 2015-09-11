'use strict';

angular.module('sampleAdminWebApp.accountApp')
    .factory('Account', function Account($resource, ACCOUNT_SERVICE_URL) {
        return $resource(ACCOUNT_SERVICE_URL, {}, {
    	//return $resource('test/account/account.json', {}, {
            'get': { method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        // expose response
                        return response;
                    }
                }
            }
        });
    });
