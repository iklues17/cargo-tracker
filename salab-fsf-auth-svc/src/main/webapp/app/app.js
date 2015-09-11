'use strict';

angular
		.module(
				'sampleAdminWebApp',
				[ 'LocalStorageModule', 'tmh.dynamicLocale', 'ngResource',
						'ui.router', 'ngCookies', 'pascalprecht.translate',
						'ngCacheBuster', 'CHSPagination',
						'nvd3ChartDirectives', 'ui.grid', 'ui.grid.cellNav',
						'ui.grid.pagination', 'ui.grid.edit',
						'ui.grid.selection', 'ui.grid.exporter',
						'ui.grid.treeView', 'ui.grid.resizeColumns',
						'ui.grid.autoResize', 'sampleAdminWebApp.sampleApp',
						'ui.bootstrap',
						'sampleAdminWebApp.accountApp' ])
 
    .run(function ($rootScope, $location, $window, $http, $state, $translate, Auth, Principal, Language, ENV, VERSION) {
        $rootScope.ENV = ENV;
        $rootScope.VERSION = VERSION;
        $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) { 	
        	if(toState.name === 'login' 
        	|| toState.name === 'register'
        	|| toState.name === 'activate'    		
        	) {
        		angular.element('body').addClass('sidebar-collapse');
        	} else {
        		angular.element('body').removeClass('sidebar-collapse');
        	}
        	
            $rootScope.toState = toState;
            $rootScope.toStateParams = toStateParams;

            if (Principal.isIdentityResolved()) {
                Auth.authorize();
            }

            // Update the language
            Language.getCurrent().then(function (language) {
                $translate.use(language);
            });
        });

        $rootScope.$on('$stateChangeSuccess',  function(event, toState, toParams, fromState, fromParams) {
            var titleKey = 'global.title';

            $rootScope.previousStateName = fromState.name;
            $rootScope.previousStateParams = fromParams;

            // Set the page title key to the one configured in state or use default one
            if (toState.data.pageTitle) {
                titleKey = toState.data.pageTitle;
            }
            $translate(titleKey).then(function (title) {
                // Change window title with translated one
                $window.document.title = title;
            });
        });

        $rootScope.back = function() {
            // If previous state is 'activate' or do not exist go to 'home'
            if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
                $state.go('home');
            } else {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            }
        };
    })
    
    .factory('authInterceptor', function ($rootScope, $q, $location, localStorageService) {
        return {
            // Add authorization token to headers
            request: function (config) {
                config.headers = config.headers || {};
                var token = localStorageService.get('token');
                
                if (token && token.expires_at && token.expires_at > new Date().getTime()) {
                    config.headers.Authorization = 'Bearer ' + token.access_token;
                }
                
                return config;
            }
        };
    })
    
    .factory('languageInterceptor', function ($rootScope, $q, $translate) {
        return {
            // Add Current Languageto headers
            request: function (config) {
            	 config.headers = config.headers || {};
            	config.headers.CurrentLanguage = $translate.use(); 
                return config;
            }
        };
    })
    
    .factory('errorInterceptor', function ($q, $rootScope, $location, $injector, ACCOUNT_SERVICE_URL, LOGOUT_SERVICE_URL) {
    	return {
        	'response': function(response) {
        		 var messageType = response.headers('X-Message-Type');
                 var messageContent = response.headers('X-Message-Content');
                 if(messageType != null && messageContent != null){
                	 $rootScope.CHSMessageType = messageType;
                	 $rootScope.CHSMessageContent = messageContent;
                	 $injector.get('$modal').open({
             			templateUrl:'app/common/error/chsmessagepage.html',
             			controller:'chsMessageController'
             		 });
                 }
        	      return response;
        	    },
            'responseError' : function(rejection) {            	
            	if(rejection.status == 401 && rejection.config.url.indexOf(ACCOUNT_SERVICE_URL) != 0) {
            		console.log('Unauthorized, Transition to login page.');
            		$injector.get('$state').transitionTo('login');	
            	} else if(rejection.status == 401 && rejection.config.url.indexOf(ACCOUNT_SERVICE_URL) == 0) {
            		console.log('Account api, Ignore error.');
            	} else if(rejection.config.url.indexOf(LOGOUT_SERVICE_URL) == 0) {
            		console.log('Logout api, Ignore error.');
            	} else if(rejection.data.message != null){
            		$rootScope.CHSErrorStatus = rejection.status;
            		$rootScope.CHSErrorCode = rejection.data.code;
            		$rootScope.CHSErrorMessage = rejection.data.message;
            		$rootScope.CHSErrorDeveloperMessage = rejection.data.developerMessage;
            		$rootScope.CHSErrorMoreInfo = rejection.data.moreInfo;
            		$rootScope.CHSErrorTId = rejection.data.tId;
            		$injector.get('$modal').open({
            			templateUrl:'app/common/error/chserrorpage.html',
        				controller:'chsErrorController'
            		});
            	}
                return $q.reject(rejection);
            }
        };
    })
    
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, $translateProvider, tmhDynamicLocaleProvider, httpRequestInterceptorCacheBusterProvider) {

        //Cache everything except rest api requests
        httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);

        $urlRouterProvider.otherwise('/');
        $stateProvider.state('site', {
            'abstract': true,
            views: {
                'header@': {
                    templateUrl: 'app/common/header/header.html',
                    controller: 'HeaderController'
                },
                'sidebar@': {
                    templateUrl: 'app/common/sidebar/sidebar.html',
                    controller: 'SidebarController'
                },
                'footer@': {
                    templateUrl: 'app/common/footer/footer.html',
                }            
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                    $translatePartialLoader.addPart('language');
                    $translatePartialLoader.addPart('error');
                    return $translate.refresh();
                }]
            }
        });
        
        $httpProvider.interceptors.push('authInterceptor');

        $httpProvider.interceptors.push('errorInterceptor');
        
        $httpProvider.interceptors.push('languageInterceptor');
        
        // Initialize angular-translate
        $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: 'i18n/{lang}/{part}.json'
        });

        $translateProvider.preferredLanguage('en');
        $translateProvider.useCookieStorage();

        tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js');
        tmhDynamicLocaleProvider.useCookieStorage('NG_TRANSLATE_LANG_KEY');
        
    }).controller('localeCtrl', function ($rootScope, $scope, $translate, $locale, tmhDynamicLocale) {
    	$translate.use('en');
    	// 언어 변경 시 호출되는 함수. translate의 {lang} 부분이 선택된 언어로 변경됨.
    	$scope.changeLanguage = function (key) {
    		$translate.use(key);
    	};
    	// 화면의 언어 선택 option 내용.
    	$rootScope.availableLocales = [
    	                               {val : 'en', translationKey: 'global.language.eng' }
    	                               , {val : 'ko', translationKey: 'global.language.kor' }
    	                               , { val : 'zh', translationKey: 'global.language.chn' } ];
    	$rootScope.model = {selectedLocale: 'en'}; // 기본 선택 옵션
    	
    	$rootScope.$locale = $locale;
    	$rootScope.changeLocale = tmhDynamicLocale.set; // locale을 선택한 옵션으로 변경
    		 
    });


