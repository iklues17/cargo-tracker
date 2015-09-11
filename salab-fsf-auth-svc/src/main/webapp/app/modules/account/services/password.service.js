'use strict';

angular.module('sampleAdminWebApp.accountApp')
    .factory('Password', function ($resource, CHANGE_PASSWORD_SERVICE_URL) {
        return $resource(CHANGE_PASSWORD_SERVICE_URL, {}, {
        });
    });
