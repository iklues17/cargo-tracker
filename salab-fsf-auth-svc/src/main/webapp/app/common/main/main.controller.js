'use strict';

angular.module('sampleAdminWebApp')
    .controller('MainController', function ($scope, Principal, AuthServerProvider) {
        Principal.identity(true).then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;

            if($scope.isAuthenticated()) {
				$scope.token = AuthServerProvider.getToken();
				AuthServerProvider.checkToken().success(function(response){
					$scope.tokenDetail = response;
				}); 
            }
        });
    });
