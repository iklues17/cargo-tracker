'use strict';

angular.module('sampleAdminWebApp.accountApp')
    .controller('FindAccountController', function ($scope, $http) {
    	
    	$scope.findUsername = function(email) {
    		console.log(email);
    		$http.post('api/get_login', {'email':email}).then(function(response){
    			$scope.findUsernameResponse = response;
    		}, function(response) {
    			$scope.findUsernameResponse = response;
    		});
    	}
    	$scope.findPassword = function(login, email) {
    		console.log(login);
    		console.log(email);
    		$http.post('api/randomize_password', {'email':email, 'login':login}).then(function(response){
    			$scope.findPasswordResponse = response;
    		}, function(response) {
    			$scope.findPasswordResponse = response;
    		});
    	}


    });

