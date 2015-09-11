(function() {
	'use strict';

	angular.module('sampleAdminWebApp.sampleApp', []).config(function($stateProvider) {
		$stateProvider.state('admin', {
			abstract : true,
			parent : 'site'
		});
	});

})();