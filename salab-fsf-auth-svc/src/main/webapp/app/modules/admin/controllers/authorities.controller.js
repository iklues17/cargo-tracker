'use strict';

angular.module('sampleAdminWebApp.sampleApp').controller('AuthoritiesController', function($timeout, $scope, Authorities, Users, Principal, Clients, uiGridConstants) {

			Principal.identity().then(function(account) {
				$scope.myAuthorities = account.roles;
			});
			
			$scope.isInRole = Principal.isInRole;
	
			$scope.authoritiesGridOptions = {};			
			$scope.authoritiesGridOptions.enableCellEditOnFocus = true;
			$scope.authoritiesGridOptions.minRowsToShow = 15;
			$scope.authoritiesGridOptions.columnDefs = [
		        {
		        	displayName:'Authorities under your authorities',
		        	field:'name', 
		        	type:'string',
		        	sort: {
		        		direction: uiGridConstants.ASC,
		        		priority: 1
		        	}
		        }
		    ];
			$scope.authoritiesGridOptions.onRegisterApi = function(gridApi) {
				$scope.gridApi = gridApi;
				gridApi.edit.on.afterCellEdit($scope, function(row, col, newVal, oldVal) {
					if(newVal !== oldVal) {
						gridApi.selection.selectRow(row);
					}
				});
				//console.log(gridApi);
			};
			$scope.addRow = function() {
				$scope.authoritiesGridOptions.data.push({'name':''});
				$timeout(function(){
					var row = $scope.authoritiesGridOptions.data[$scope.authoritiesGridOptions.data.length-1];
					var col = $scope.authoritiesGridOptions.columnDefs[0];
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
				Authorities.save(rows).$promise.then(function(){
					$scope.reset();
					$scope.load4();
					$scope.load10();
					alert("saved");
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
				var param = {'authorities':''};
				for(var row in rows) {
					param['authorities'] += rows[row].name+",";
				}
				Authorities.delete(param).$promise.then(function(){
					$scope.reset();
					$scope.load4();
					$scope.load10();
					alert("deleted");
				}, function() {
					alert("error");
				});
			};
			
			$scope.reset = function() {
				$scope.authoritiesGridOptions.data = Authorities.getGrantableByMe();
			};		

			
			
			$scope.usersGridOptions = {};
			//$scope.usersGridOptions.enableRowHeaderSelection = false;	
			$scope.usersGridOptions.paginationPageSizes = [10, 50, 100];
			$scope.usersGridOptions.paginationPageSize = 10;
			$scope.usersGridOptions.enableHorizontalScrollbar = uiGridConstants.scrollbars.NEVER;
			$scope.usersGridOptions.useExternalPagination = true;
			//$scope.usersGridOptions.enableVerticalScrollbar = uiGridConstants.scrollbars.NEVER;
			
			$scope.usersGridOptions.enableCellEditOnFocus = true;
			$scope.usersGridOptions.columnDefs = [
	            {field:'id', visible:false, type:'string'},
				{
	            	field:'login',
	            	type:'string',
		            sort: {
		        		direction: uiGridConstants.ASC,
		        		priority: 1
	        		}
	            },
	        	{field:'firstName', type:'string'},
	        	{field:'lastName', type:'string'},
	        	{field:'email', type:'string'},
	        	{field:'mobilePhoneNumber', type:'string'},
	        	{field:'langKey', type:'string'},
				{field:'activated', type:'boolean'},
				{field:'activatedKey', type:'string'},
				{field:'authorityBase', type:'string'},
				{field:'password', type:'string'},
				{field:'passwordUpdatedDate', type:'string'},
				{field:'passwordExpiredDate', type:'string'},
				{field:'loginFailureCount', type:'string'}
			];
			$scope.usersGridOptions.minRowsToShow = 12;
			$scope.usersGridOptions.onRegisterApi = function(gridApi) {
				$scope.gridApi2 = gridApi;
				gridApi.edit.on.afterCellEdit($scope, function(row, col, newVal, oldVal) {
					if(newVal !== oldVal) {
						gridApi.selection.selectRow(row);
					}
				});
				gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
					getUsersPage2(newPage, pageSize);
				});
			};
			$scope.addRow2 = function() {
				$scope.usersGridOptions.data.push({'activated':true,'langKey':'en'});
				$timeout(function(){
					var row = $scope.usersGridOptions.data[$scope.usersGridOptions.data.length-1];
					var col = $scope.usersGridOptions.columnDefs[1];
					$scope.gridApi2.selection.selectRow(row);
					$scope.gridApi2.cellNav.scrollToFocus(row, col);
				},0);				
			};
			$scope.saveRows2 = function() {
				var rows = $scope.gridApi2.selection.getSelectedRows();
				if(rows.length == 0) {
					alert("줄을 선택해주세요.");
					return;
				}
				Users.save(rows).$promise.then(function(){
					$scope.reset2();
					$scope.reset3();
					alert("saved");
				}, function() {
					alert("error");
				});
			};

			$scope.deleteRows2 = function() {
				var rows = $scope.gridApi2.selection.getSelectedRows();
				if(rows.length == 0) {
					alert("줄을 선택해주세요.");
					return;
				}
				var param = {'users':''};
				for(var row in rows) {
					param['users'] += rows[row].id+",";
				}
				Users.delete(param).$promise.then(function(){
					$scope.reset2();
					$scope.reset3();
					alert("deleted");
				}, function() {
					alert("error");
				});
			};
			
			var getUsersPage2 = function(newPage, pageSize) {
				Users.query({page: newPage, per_page: pageSize}, function(data, headers) {
					$scope.usersGridOptions.data = data;
					$scope.usersGridOptions.totalItems = parseInt(headers('X-Total-Count'));
				});
			};		

			$scope.reset2 = function(){
				getUsersPage2($scope.usersGridOptions.paginationCurrentPage, $scope.usersGridOptions.paginationPageSize);
			};
			
			

			
			
			$scope.usersGridOptions2 = {};
			$scope.usersGridOptions2.paginationPageSizes = [10, 50, 100];
			$scope.usersGridOptions2.paginationPageSize = 10;
			//$scope.usersGridOptions2.enableHorizontalScrollbar = uiGridConstants.scrollbars.NEVER;
			$scope.usersGridOptions2.useExternalPagination = true;
			$scope.usersGridOptions2.enableRowHeaderSelection = false;	
			$scope.usersGridOptions2.multiSelect = false;
			$scope.usersGridOptions2.columnDefs = [
			    {
			    	field:'login',
			    	displayName:'User Name', 
			    	type:'string',
		        	sort: {
		        		direction: uiGridConstants.ASC,
		        		priority: 1
		        	}
			   }
			];
			$scope.usersGridOptions2.minRowsToShow = 10;
			$scope.usersGridOptions2.onRegisterApi = function(gridApi) {
				$scope.gridApi3 = gridApi;
				gridApi.selection.on.rowSelectionChanged($scope, function(row){
					$scope.load4(row.entity);			
				});
				gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
					getUsersPage3(newPage, pageSize);
				});
			};

			$scope.authoritiesGridOptions2 = {};
			$scope.authoritiesGridOptions2.multiSelect = true;
			$scope.authoritiesGridOptions2.columnDefs = [
			    {
			    	field:'name',
			    	displayName:'Grantables',	
			    	type:'string',
					sort: {
		        		direction: uiGridConstants.ASC,
		        		priority: 1
		        	}
				}
			];
			$scope.authoritiesGridOptions2.minRowsToShow = 10;
			$scope.authoritiesGridOptions2.onRegisterApi = function(gridApi) {
				$scope.gridApi4 = gridApi;
			};		

			$scope.saveRows3 = function() {
				var targetUsers = $scope.gridApi3.selection.getSelectedRows();
				var authoritiesToGrant = $scope.gridApi4.selection.getSelectedRows();
				if(targetUsers.length != 1) {
					alert("계정을 선택해주세요.");
					return;
				}
				Authorities.setGrantableByMeGrantTo({id:targetUsers[0].id}, authoritiesToGrant).$promise.then(function(){
					$scope.load4(targetUsers[0]);
					alert("saved");
				}, function() {
					alert("error");
				});
			};
			var getUsersPage3 = function(newPage, pageSize) {
				Users.query({page: newPage, per_page: pageSize}, function(data, headers) {
					$scope.usersGridOptions2.data = data;
					$scope.usersGridOptions2.totalItems = parseInt(headers('X-Total-Count'));
				});
			};		
			$scope.reset3 = function(){
				getUsersPage3($scope.usersGridOptions2.paginationCurrentPage, $scope.usersGridOptions2.paginationPageSize);
				$scope.authoritiesGridOptions2.data = Authorities.getGrantableByMe();
			};			
			
			$scope.load4 = function(user) {
				var grantableAuthorities = Authorities.getGrantableByMe(function() {	
					$scope.authoritiesGridOptions2.data = grantableAuthorities;
					if(undefined !== user) {
						
					} else if($scope.gridApi3.selection.getSelectedRows()){
						user = $scope.gridApi3.selection.getSelectedRows()[0];
					}
					
					if(undefined !== user) {
						var grantedAuthorities = Authorities.getGrantableByMeGrantTo({id:user.id}, function() {
							for(var i in grantedAuthorities) {
								if(grantedAuthorities[i].constructor.name == 'Resource') {
									angular.forEach($scope.authoritiesGridOptions2.data, function(row) {
										if(row.name == grantedAuthorities[i].name) {
											$scope.gridApi4.selection.toggleRowSelection(row);
										}
									});	
								}
							}
						});
					}
				});		
			}
			$scope.addRow4 = function() {
				$scope.authoritiesGridOptions2.data.push({'name':'ROLE_'});
			};

		
			
			
			
			
			$scope.clientsGridOptions11 = {};
			$scope.clientsGridOptions11.enableRowHeaderSelection = false;	
			$scope.clientsGridOptions11.multiSelect = false;
			$scope.clientsGridOptions11.columnDefs = [
				{
					field:'clientId',
					displayName:'Client Name', 
					type:'string',			        	
					sort: {
						direction: uiGridConstants.ASC,
						priority: 1
					}
				}
			];
			$scope.clientsGridOptions11.minRowsToShow = 10;
			$scope.clientsGridOptions11.onRegisterApi = function(gridApi) {
				$scope.gridApi11 = gridApi;
				gridApi.selection.on.rowSelectionChanged($scope, function(row){
					$scope.load10(row.entity);			
				});
			};

			$scope.authoritiesGridOptions12 = {};
			$scope.authoritiesGridOptions12.multiSelect = true;
			$scope.authoritiesGridOptions12.columnDefs = [
				{
					field:'name', 
					displayName:'Grantables', 
					type:'string',			        	
						sort: {
							direction: uiGridConstants.ASC,
							priority: 1
					}
				}
			];
			$scope.authoritiesGridOptions12.minRowsToShow = 10;
			$scope.authoritiesGridOptions12.onRegisterApi = function(gridApi) {
				$scope.gridApi12 = gridApi;
			};		
		
			$scope.reset10 = function() {
				$scope.clientsGridOptions11.data = Clients.query();
				$scope.authoritiesGridOptions12.data = Authorities.getGrantableByMe();				
			};	
			$scope.load10 = function(client) {
				var grantableAuthorities = Authorities.getGrantableByMe(function() {	
					$scope.authoritiesGridOptions12.data = grantableAuthorities;
					if(undefined !== client) {
						
					} else if($scope.gridApi11.selection.getSelectedRows()){
						client = $scope.gridApi11.selection.getSelectedRows()[0];
					}
					
					if(undefined !== client) {
						var grantedAuthorities = Authorities.getAsClientGrantDefaultUserAuthoritiesTo({clientId:client.clientId}, function() {
							for(var i in grantedAuthorities) {
								if(grantedAuthorities[i].constructor.name == 'Resource') {
									angular.forEach($scope.authoritiesGridOptions12.data, function(row) {
										if(row.name == grantedAuthorities[i].name) {
											$scope.gridApi12.selection.toggleRowSelection(row);
										}
									});	
								}
							}
						});
					}
				});		
			}			
			$scope.saveRows10 = function() {
				var targetclients = $scope.gridApi11.selection.getSelectedRows();
				var authoritiesToGrant = $scope.gridApi12.selection.getSelectedRows();
				if(targetclients.length != 1) {
					alert("계정을 선택해주세요.");
					return;
				}
				Authorities.setAsClientGrantDefaultUserAuthoritiesTo({clientId:targetclients[0].clientId}, authoritiesToGrant).$promise.then(function(){
					$scope.load10(targetclients[0]);
					alert("saved");
				}, function() {
					alert("error");
				});
			};			
			
			
			$scope.$on('$viewContentLoaded', function(){
				$scope.reset();
				$scope.reset2();
				$scope.reset3();
				
				$scope.reset10();
			});
		});
