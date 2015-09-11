'use strict';

angular.module('sampleAdminWebApp.accountApp').controller('LoginController', function (Principal, $location, $rootScope, $scope, $state, $timeout, Auth, AuthServerProvider, localStorageService) {

		$scope.$on('$viewContentLoaded', function() {
			$scope.getDisplayInfo();
		});
		
		$scope.getDisplayInfo = function() {
			if(AuthServerProvider.hasValidToken() && null != AuthServerProvider.getToken()){
				AuthServerProvider.checkToken().success(function(response){
					$scope.isLogon = true;
					$scope.token = AuthServerProvider.getToken();
					$scope.tokenDetail = response;
				}).error(function(response){
					Auth.logout();
					$scope.isLogon = false;
				});
			} else {
				Auth.logout();
				$scope.isLogon = false;
			}
			
			Principal.identity().then(function(account){
				$scope.setPasswordExpired(account);
			});	
			
			if(undefined != $location.search().grant_type){
				$scope.grant_type = $location.search().grant_type;
			} else {
				$scope.grant_type = 'password';
			}
			
			$scope.isFormReadOnly = false;
			if('client_credentials' == $scope.grant_type
			|| 'implicit' == $scope.grant_type
			|| 'authorization_code' == $scope.grant_type
			) {
				$scope.isFormReadOnly = true;
			}
		}
		
		$scope.setPasswordExpired = function(account){
        	if(account != null && account.infos.passwordExpired) {
        		$scope.passwordExpired = account.infos.passwordExpired;
        		return $scope.passwordExpired;
        	}
		}
		$scope.goToChangePassword = function(){
			$state.go('password');
		}		
		
		
		$scope.user = {};
        $scope.errors = {};

        $scope.rememberMe = true;
        $timeout(function (){angular.element('[ng-model="username"]').focus();});
        $scope.login = function () {
        		
            var loginPromise = Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            })
            
            if(!loginPromise) return;
            
            loginPromise.then(function (account) {  	
                $scope.authenticationError = false;   
                $scope.response = null;
            	
                if(account.infos.passwordExpired) {
                	$scope.getDisplayInfo();
                	return;
                }
                
                if ($rootScope.previousStateName === 'register' || $rootScope.previousStateName === 'login') {
                    $state.go('home');
                } else if(undefined !== $rootScope.returnToState) {
                	$state.go($rootScope.returnToState.name, $rootScope.returnToStateParams);
                	$rootScope.returnToState = undefined;
                	$rootScope.returnToStateParams = undefined;
                } else {
                    $rootScope.back();
                }                
               
            }).catch(function (response) {
                $scope.authenticationError = true;
                $scope.response = response;
            });
        };
        $scope.logout = function() {
        	Auth.logout().finally(function () {
                $state.go($state.current, {}, {location: false, reload: true});
            });
        };
    });
