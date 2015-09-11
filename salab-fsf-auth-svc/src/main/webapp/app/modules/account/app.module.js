(function() {
	'use strict';

	angular.module('sampleAdminWebApp.accountApp', []).config(function($stateProvider) {
		$stateProvider.state('account', {
			abstract : true,
			parent : 'site'
		});
	});

})();