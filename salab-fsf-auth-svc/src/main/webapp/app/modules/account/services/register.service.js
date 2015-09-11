'use strict';

angular.module('sampleAdminWebApp.accountApp')
    .factory('Register', function ($resource, REGISTER_SERVICE_URL) {
        return $resource(REGISTER_SERVICE_URL, {}, {
        });
    });


