'use strict';

angular.module('sampleAdminWebApp.accountApp')
    .controller('ActivationController', function ($scope, $stateParams, Auth, $timeout) {
   	
    	$scope.focusOnKeyInputBox = function(){
    		$timeout(function (){angular.element('[ng-model="paramKey"]').focus();});
    	} 		
    	$scope.activateAccount = function() {
    		if(!$scope.paramKey) {
    			$scope.focusOnKeyInputBox();
    			return;
    		}
            Auth.activateAccount({key: $scope.paramKey}).then(function () {
                $scope.error = false;
                $scope.success = 'OK';
                $scope.paramKey = null;
            }).catch(function () {
                $scope.success = false;
                $scope.error = 'ERROR';
                $scope.focusOnKeyInputBox();
            });		
    	}    
    	
    	$scope.paramKey = $stateParams.key;
    	$scope.activateAccount();

    });

