'use strict';

angular.module('sampleAdminWebApp.accountApp')
    .factory('Activate', function ($resource, ACTIVATE_SERVICE_URL) {
        return $resource(ACTIVATE_SERVICE_URL, {}, {
            'get': { method: 'GET', params: {}, isArray: false}
        });
    });


