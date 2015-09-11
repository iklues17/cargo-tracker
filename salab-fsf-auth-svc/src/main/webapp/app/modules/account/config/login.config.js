'use strict';

angular.module('sampleAdminWebApp.accountApp')
    .config(function ($stateProvider) {    	
        $stateProvider
            .state('login', {
                parent: 'account',
                url: '/login',
                data: {
                    roles: [], 
                    pageTitle: 'login.title'
                },
                views: {                   
                    'content@': {
                        templateUrl: 'app/modules/account/views/login.view.html',
                        controller: 'LoginController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('login');
                        return $translate.refresh();
                    }]
                }
            });
    });
