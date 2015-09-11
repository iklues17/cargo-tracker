'use strict';

angular.module('sampleAdminWebApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('logout', {
                parent: 'account',
                url: '/logout',
                data: {
                    roles: []
                },
                views: {
                    'header@': {
                        templateUrl: null,
                        controller: null
                    },
                    'sidebar@': {
                        templateUrl: null,
                        controller: null
                    },
                    'footer@': {
                        templateUrl: null,
                        controller: null
                    },                     	
                    'content@': {
                        templateUrl: 'app/modules/account/views/logout.view.html',
                        controller: 'LogoutController'
                    }
                }
            });
    });
