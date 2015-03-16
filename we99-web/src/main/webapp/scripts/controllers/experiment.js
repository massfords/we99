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
    
    //retrieve list of experiments
    RestService.getExperiments()
    	.success(function(response){
    		$scope.experiments=response.values;
    		$scope.displayExperiments=[].concat($scope.experiments);
    		})
    	.error(function(response){
    		$scope.errorText="Could not retrieve experiments list.";
    		});
    
    //delete experiment from database
    $scope.removeItem=function(row){
    	RestService.deleteExperiment(row.id)
    		.success(function(response){
	    		for(var i=0;i<$scope.experiments.length;i++){
	    			if($scope.experiments[i].id===row.id)
	    				$scope.experiments.splice(i,1);
	    				break;
	    			}
    		})
    		.error(function(response){
    		   console.log('Error: '+response);
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
