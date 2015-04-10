'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:ExperimentCtrl
 * @description
 * # ExperimentCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('ExperimentListCtrl', ['$scope','$rootScope','$location', 'RestService',function ($scope,$rootScope,$location,RestService) {


    //retrieve list of experiments
    RestService.getExperiments()
    	.success(function(response){
    		$scope.experiments=response.values;
    		$scope.displayExperiments=[].concat($scope.experiments);
    		})
    	.error(function(response){
    		$scope.errorText="Could not retrieve experiments list.";
    		});

    $scope.editRow=function(){
      for(var i=0;i<$scope.experiments.length;i++)
        if ($scope.experiments[i].isSelected) {
          $location.path('/experiment/addedit/' + $scope.experiments[i].id);
          break;
      }
    };

    // Deletes an experiment from database
    $scope.removeItem=function(row){
    	RestService.deleteExperiment(row.id)
    		.success(function(response){
	    		for(var i=0;i<$scope.experiments.length;i++)
            if ($scope.experiments[i].id === row.id) {
              $scope.experiments.splice(i, 1);
              break;
          }
    		})
    		.error(function(response){
    		   console.log('Error: '+response);
    		});
    };

    // publish an experiment
    $scope.publish = function(experiment) {
      var confirmed = confirm('Are you sure you want to publish?\n' +
                              'This will lock down the experiment data.');
      if (confirmed) {
        RestService.publishExperiment(experiment.id).then(function success() {
          experiment.status = 'PUBLISHED';
        }, function fail(err) {
          console.error(err);
          alert('Could not publish.');
        });
      }
    };

    // fired when table rows are selected
    $scope.$watch('displayExperiments', function(newVal) {
      if(newVal){
        var list=newVal.filter(function(item) {
          return item.isSelected;
        });
        if(list.length==0)
          $rootScope.currentExperiment=null;
        else
          $rootScope.currentExperiment=list[0];
      }

    }, true);


    $scope.dismiss=function(type){
    	if(type==='info'){
    		$scope.infoText=null;
    	}
    	else if(type==='error')
    		$scope.errorText=null;
    }

  }]);
