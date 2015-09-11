'use strict';

angular.module('sampleAdminWebApp.accountApp')
    .controller('LogoutController', function (Auth, $state) {
        Auth.logout().then(function() {
        	$state.go('home');
        });
    });
