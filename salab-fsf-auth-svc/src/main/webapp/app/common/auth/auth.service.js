'use strict';

angular.module('sampleAdminWebApp')
    .factory('Auth', function Auth($rootScope, $state, $q, $translate, Principal, AuthServerProvider, Account, Register, Activate, Password) {
        return {
            login: function (credentials, callback) {
                var cb = callback || angular.noop;
                var deferred = $q.defer();

                var AuthServerProviderPromise = AuthServerProvider.login(credentials)
                
                if(!AuthServerProviderPromise) return;
                
                AuthServerProviderPromise.then(function (data) {
                    // retrieve the logged account information
                    Principal.identity(true).then(function(account) {
                        // After the login the language will be changed to
                        // the language selected by the user during his registration
                    	if(account && account.langKey) {
                    		$translate.use(account.langKey);
                    	}
                    	deferred.resolve(account);
                    });
                    return cb();
                }).catch(function (err) {
                    this.logout();
                    deferred.reject(err);
                    return cb(err);
                }.bind(this));

                return deferred.promise;
            },

            logout: function () { 	
            	var deferred = $q.defer();
                AuthServerProvider.logout().finally(function(){
                	Principal.authenticate(null);
                	setTimeout(function(){
                		deferred.resolve();
                	}, 1000);
                });
                return deferred.promise;
            },

            authorize: function() {
                return Principal.identity()
                    .then(function() {
                        var isAuthenticated = Principal.isAuthenticated();


                        if ($rootScope.toState.data.roles && $rootScope.toState.data.roles.length > 0 && !Principal.isInAnyRole($rootScope.toState.data.roles)) {
                            if (isAuthenticated) {
                                // user is signed in but not authorized for desired state
                                $state.go('accessdenied');
                            }
                            else {
                                // user is not authenticated. stow the state they wanted before you
                                // send them to the signin state, so you can return them when you're done
                                $rootScope.returnToState = $rootScope.toState;
                                $rootScope.returnToStateParams = $rootScope.toStateParams;

                                // now, send them to the signin state so they can log in
                                $state.go('login');
                            }
                        }
                        
                    });
            },
            createAccount: function (account, callback) {
                var cb = callback || angular.noop;

                return Register.save(account,
                        function () {
                            return cb(account);
                        },
                        function (err) {
                            this.logout();
                            return cb(err);
                        }.bind(this)).$promise;                
            },

            updateAccount: function (account, callback) {
                var cb = callback || angular.noop;

                return Account.save(account,
                    function () {
                        return cb(account);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
            },

            activateAccount: function (key, callback) {
                var cb = callback || angular.noop;

                return Activate.get(key,
                    function (response) {
                        return cb(response);
                    },
                    function (err) {
                        return cb(err);
                    }.bind(this)).$promise;
            },

            changePassword: function (newPassword, callback) {
                var cb = callback || angular.noop;

                return Password.save(newPassword, function () {
                    return cb();
                }, function (err) {
                    return cb(err);
                }).$promise;
            }
        };
    });
