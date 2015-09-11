'use strict';

angular.module('sampleAdminWebApp.sampleApp').controller('ClientsController', function(uiGridConstants, $timeout, $scope, Clients) {

			$scope.gridOptions = {};
			$scope.gridOptions.enableCellEditOnFocus = true;
			$scope.gridOptions.minRowsToShow = 20;
			$scope.gridOptions.columnDefs = [
  				{
  	            	field:'clientId', 
  	            	type:'string',
  		            sort: {
  		        		direction: uiGridConstants.ASC,
  		        		priority: 1
  	        		}
  	            },
  	        	{field:'clientSecret', type:'string'},
  	        	{field:'resourceIds', type:'string'},
  	        	{field:'scope', type:'string'},
  				{field:'authorizedGrantTypes', type:'string'},
  				{field:'webServerRedirectUri', type:'string'},
  				{field:'authorities', type:'string'},
  				{field:'accessTokenValidity', type:'string'},
  				{field:'refreshTokenValidity', type:'string'},
  				{field:'additionalInformation', type:'string'},
  				{field:'autoapprove', type:'string'},
  			];
			$scope.$on('$viewContentLoaded', function(){
				$scope.gridOptions.data = Clients.query();
			});

			$scope.gridOptions.onRegisterApi = function(gridApi) {
				$scope.gridApi = gridApi;
				gridApi.edit.on.afterCellEdit($scope, function(row, col, newVal, oldVal) {
					if(newVal !== oldVal) {
						gridApi.selection.selectRow(row);
					}
				});
			};
			$scope.addRow = function() {
				$scope.gridOptions.data.push({'accessTokenValidity': 1600, 'refreshTokenValidity': 16000});
				$timeout(function(){
					var row = $scope.gridOptions.data[$scope.gridOptions.data.length-1];
					var col = $scope.gridOptions.columnDefs[0];
					$scope.gridApi.selection.selectRow(row);
					$scope.gridApi.cellNav.scrollToFocus(row, col);
				},0);				
			};
			$scope.saveRows = function() {
				var rows = $scope.gridApi.selection.getSelectedRows();
				if(rows.length == 0) {
					alert("줄을 선택해주세요.");
					return;
				}
				Clients.save(rows).$promise.then(function(){
					$scope.reset();
				}, function() {
					alert("error");
				});
			};

			$scope.deleteRows = function() {
				var rows = $scope.gridApi.selection.getSelectedRows();
				if(rows.length == 0) {
					alert("줄을 선택해주세요.");
					return;
				}
				var param = {'clientIds':''};
				for(var row in rows) {
					param['clientIds'] += rows[row].clientId+",";
				}
				Clients.delete(param).$promise.then(function(){
					$scope.reset();
				}, function() {
					alert("error");
				});
			};
			
			$scope.reset = function() {
				$scope.gridOptions.data = Clients.query();
			};			
			
		});
