
'use strict';

angular.module('CHSPagination', [])
	.service('CHSPaginationUtil', function(){
		this.getPageInfo = function(gridOptions){
			// calculate paging
			var current = gridOptions.pageNumber;
			var perPage = gridOptions.paginationPageSize;
			var totalPage = gridOptions.totalItems;
			var paginationPageSizes = gridOptions.paginationPageSizes;
			var first = 1;
			var prev = first;
			if(current - 1 >= first){
				prev = current - 1;
			}else{
				prev = null;
			}
			var last = parseInt(totalPage / perPage);
			if(totalPage % perPage != 0){
				last++;
			}
			var next = last;
			if(current + 1 <= last){
				next = current + 1;
			}else{
				next = null;
			}
			
			var firstItem = 0;
			var lastItem = 0;
			firstItem = (current - 1) * perPage + 1;
			lastItem = (current) * perPage;
			if(lastItem > totalPage){
				lastItem = totalPage;
			}
			
			var pages = {};
			pages.first = first;
			pages.prev = prev;
			pages.next = next;
			pages.last = last;
			pages.current = current;
			
			pages.firstItem = firstItem;
			pages.lastItem = lastItem;
			pages.totalPage = totalPage;
			pages.paginationPageSizes = paginationPageSizes;
			pages.perPage = perPage;
			return pages;
		};
		
		this.getPageInfoForTable = function(paginationOptions){
			// calculate paging
			var current = paginationOptions.pageNumber;
			var perPage = paginationOptions.pageSize;
			var totalPage = paginationOptions.totalItems;
			var paginationPageSizes = paginationOptions.paginationPageSizes;
			var first = 1;
			var prev = first;
			if(current - 1 >= first){
				prev = current - 1;
			}else{
				prev = null;
			}
			var last = parseInt(totalPage / perPage);
			if(totalPage % perPage != 0){
				last++;
			}
			var next = last;
			if(current + 1 <= last){
				next = current + 1;
			}else{
				next = null;
			}
			
			var firstItem = 0;
			var lastItem = 0;
			firstItem = (current - 1) * perPage + 1;
			lastItem = (current) * perPage;
			if(lastItem > totalPage){
				lastItem = totalPage;
			}
			
			var pages = {};
			pages.first = first;
			pages.prev = prev;
			pages.next = next;
			pages.last = last;
			pages.current = current;
			
			pages.firstItem = firstItem;
			pages.lastItem = lastItem;
			pages.totalPage = totalPage;
			pages.paginationPageSizes = paginationPageSizes;
			pages.perPage = perPage;
			console.log(pages);
			return pages;
		};
	})
	.service('chsPagingSvc', function($rootScope){
		var newValue;
		this.broadcast = function(){$rootScope.$broadcast('pagingChanged');};
	})
	.service('chsPagingTableSvc', function($rootScope){
		var newValue;
		this.broadcast = function(){$rootScope.$broadcast('pagingTableChanged');};
	})
	.controller('chs.paging.ctrl', function($scope, chsPagingSvc){
		// select option changed
		$scope.pagingChanged = function(perPage){
			$scope.perPage = perPage;
			$scope.gridOptions.paginationPageSize = $scope.perPage;
			chsPagingSvc.newValue =  perPage;
			chsPagingSvc.broadcast();
		};
		$scope.$on('pagingChanged', function(){
			$scope.perPage = chsPagingSvc.newValue;
		});
	})
	.controller('chs.paging.header.ctrl', function($scope, chsPagingSvc){
		// select option changed
		$scope.pagingChanged = function(perPage){
			$scope.perPage = perPage;
			$scope.gridOptions.paginationPageSize = $scope.perPage;
			chsPagingSvc.newValue =  perPage;
			chsPagingSvc.broadcast();
		};
		$scope.$on('pagingChanged', function(){
			$scope.perPage = chsPagingSvc.newValue;
		});
	})
	.controller('chs.paging.table.ctrl', function($scope, chsPagingTableSvc){
		// select option changed
		$scope.pagingChanged = function(perPage){
			$scope.perPage = perPage;
			$scope.paginationOptions.pageSize = $scope.perPage;
			$scope.loadPage(1);
			
			chsPagingTableSvc.newValue = perPage;
			chsPagingTableSvc.broadcast();
		};
		$scope.$on('pagingTableChanged', function(){
			$scope.perPage = chsPagingTableSvc.newValue;
		});
	})
	.controller('chs.paging.table.header.ctrl', function($scope, chsPagingTableSvc){
		// select option changed
		$scope.pagingChanged = function(perPage){
			$scope.perPage = perPage;
			$scope.paginationOptions.pageSize = $scope.perPage;
			$scope.loadPage(1);
			
			chsPagingTableSvc.newValue = perPage;
			chsPagingTableSvc.broadcast();
		};
		$scope.$on('pagingTableChanged', function(){
			$scope.perPage = chsPagingTableSvc.newValue;
		});
	})
	.directive('chs.pagination', function() {
		return {
			restrict: 'E',
			scope: false, 
			templateUrl: 'app/common/pagination/CHSPagination.tpl.html'
	  }
	})
	.directive('chs.page.info', function() {
		return {
			restrict: 'E',
			scope: false, 
			templateUrl: 'app/common/pagination/CHSPagination.header.tpl.html'
		}
	})
	.directive('chs.pagination.table', function() {
		return {
			restrict: 'E',
			scope: false, 
			templateUrl: 'app/common/pagination/CHSPagination.table.tpl.html'
		}
	})
	.directive('chs.page.info.table', function() {
			return {
				restrict: 'E',
//				scope: false, 
				templateUrl: 'app/common/pagination/CHSPagination.table.header.tpl.html'
			}
	})
;