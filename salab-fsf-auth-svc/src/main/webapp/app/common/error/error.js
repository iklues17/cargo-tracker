'use strict';

angular.module('sampleAdminWebApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('error', {
                parent: 'site',
                url: '/error',
                data: {
                    roles: [],
                    pageTitle: 'errors.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/common/error/error.html'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('error');
                        return $translate.refresh();
                    }]
                }
            })
            .state('accessdenied', {
                parent: 'site',
                url: '/accessdenied',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'app/common/error/accessdenied.html'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('error');
                        return $translate.refresh();
                    }]
                }
            });
    })
    .controller('chsMessageController', function($scope, $rootScope, $modalInstance){
    	$scope.type = $rootScope.CHSMessageType;
    	$scope.message = $rootScope.CHSMessageContent;
    	$scope.close = function(){
    		$modalInstance.close();
    	}
    })
    .controller('chsErrorController', function($scope, $rootScope, $modalInstance){
    	$scope.isCollapsed = true;
    	$scope.status = $rootScope.CHSErrorStatus;
    	$scope.code = $rootScope.CHSErrorCode;
    	$scope.message = $rootScope.CHSErrorMessage;
    	$scope.developerMessage = $rootScope.CHSErrorDeveloperMessage;
    	$scope.moreInfo = $rootScope.CHSErrorMoreInfo;
    	$scope.tId = $rootScope.CHSErrorTId;
    	$scope.close = function(){
    		$modalInstance.close();
    	}
    })
    ;
