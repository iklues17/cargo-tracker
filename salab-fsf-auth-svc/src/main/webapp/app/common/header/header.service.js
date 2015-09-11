'use strict';

angular.module('sampleAdminWebApp')
	.factory( 'Alert', function ( $rootScope, $timeout ) {
		var alertService;
		$rootScope.alerts = [];
		return alertService = {
			add : function(type, msg) {
				var alert = {
						type : type,
						msg : msg,
						show : true,
						close : function() {
							return alertService.closeAlert(this);
						}
				};
				$rootScope.alerts.push( alert );
				var index = $rootScope.alerts.indexOf(alert);
				$timeout ( function () {
					$rootScope.alerts[index].show = false;
		        }, 2000);
				console.log(window.setTimeout);
				return;
			},
			closeAlert : function(alert) {
				return this.closeAlertIdx($rootScope.alerts
						.indexOf(alert));
			},
			closeAlertIdx : function(index) {
				return $rootScope.alerts.splice(index, 1);
			},
			clear : function() {
				$rootScope.alerts = [];
			}
		};
	} );