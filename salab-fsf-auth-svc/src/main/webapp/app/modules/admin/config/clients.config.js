'use strict';

angular.module('sampleAdminWebApp.sampleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('clients', {
                parent: 'admin',
                url: '/admin/clients',
                data: {
                    roles: ['ROLE_AUTH_USER'],
                    pageTitle: 'sampleAdminWebApp.clients.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/modules/admin/views/clients.view.html',
                        controller: 'ClientsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('clients');
                        return $translate.refresh();
                    }]
                }
            });
    });
