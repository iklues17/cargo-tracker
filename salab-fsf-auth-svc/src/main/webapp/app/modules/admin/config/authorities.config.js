'use strict';

angular.module('sampleAdminWebApp.sampleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('authorities', {
                parent: 'admin',
                url: '/admin/authorities',
                data: {
                    roles: ['ROLE_AUTH_USER'],
                    pageTitle: 'sampleAdminWebApp.authorities.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/modules/admin/views/authorities.view.html',
                        controller: 'AuthoritiesController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('authorities');
                        return $translate.refresh();
                    }]
                }
            });
    });
