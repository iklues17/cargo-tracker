'use strict';

angular.module('sampleAdminWebApp').factory(
		'datePickerUtil', function() {

			var method = {};
			method.instances = [];

			method.open = function($event, instance) {
				$event.preventDefault();
				$event.stopPropagation();

				method.instances[instance] = true;
			};
			method.options = {
				'show-weeks' : false,
				startingDay : 0
			};

			//var formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
			//method.format = formats[0];

			return method;
		});
