'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:ExperimentCtrl
 * @description
 * # ExperimentCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('ExperimentListCtrl', ['$scope', '$http','$location', 'RestService',function ($scope,$http,$location,RestService) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
    
    $http.get('services/rest/experiment')
    	.success(function(response){
    		$scope.experiments=response.values;
    		$scope.displayExperiments=[].concat($scope.experiments);
    		})
    	.error(function(response){
    		$scope.errorText="Could not retrieve experiments list.";
    		});
    
    $scope.removeItem=function(row){
    	$http.delete('services/rest/experiment/'+row.id)
    		.success(function(response){
	    		var index=$scope.experiments.indexOf(row);
	    		$scope.experiments.splice(index);
    		})
    		.error(function(response){
    		
    		});
    }
    
    
    $scope.dismiss=function(type){
    	if(type==='info'){
    		$scope.infoText=null;
    	}
    	else if(type==='error')
    		$scope.errorText=null;
    }
    
  }]);
