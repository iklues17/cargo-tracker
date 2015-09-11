'use strict';

angular.module('sampleAdminWebApp')
    .controller('HeaderController', function ($scope, $location, $state, Auth, Principal, Alert) {
    	
    	$scope.closeAlert = function ( index ) {
    		Alert.closeAlert( index );
    	}
    	$scope.clearAlert = function () {
    		Alert.clear();
    	}
    	
    	$scope.identity = Principal.identity;
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.isInRole = Principal.isInRole;
        $scope.$state = $state;

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.$on('principalIdentityChanged', function(event, account){
        	$scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        
        if ($.AdminLTE.options.sidebarPushMenu) {
            $.AdminLTE.pushMenu($.AdminLTE.options.sidebarToggleSelector);
          }
        
    });
