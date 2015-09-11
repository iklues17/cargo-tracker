'use strict';

angular.module('sampleAdminWebApp.accountApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('findAccount', {
                parent: 'account',
                url: '/find_account',
                data: {
                    roles: ['ROLE_AUTH_USER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/modules/account/views/findAccount.view.html',
                        controller: 'FindAccountController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('activate');
                        return $translate.refresh();
                    }]
                }
            });
    });

