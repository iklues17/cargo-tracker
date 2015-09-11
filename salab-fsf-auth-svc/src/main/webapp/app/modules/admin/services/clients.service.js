'use strict';

angular.module('sampleAdminWebApp.sampleApp')
    .factory('Clients', function ($resource, CLIENTS_SERVICE_URL) {
    	return $resource(CLIENTS_SERVICE_URL, {}, {
			});
    });
