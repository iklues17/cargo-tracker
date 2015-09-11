'use strict';

angular.module('sampleAdminWebApp')
    .controller('SidebarController', function ($scope, $location, $state, Auth, Principal) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.isInRole = Principal.isInRole;    	
        $scope.$state = $state;

        $.AdminLTE.tree('.sidebar');

    });
