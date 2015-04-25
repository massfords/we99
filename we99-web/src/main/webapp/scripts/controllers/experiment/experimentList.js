'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:ExperimentCtrl
 * @description
 * # ExperimentCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('ExperimentListCtrl', ['$scope','$timeout','$rootScope','$location', 'RestService','TourConstants',
      function ($scope,$timeout,$rootScope,$location,RestService,TourConstants) {
    var waitingForSecondClick = false;

    //retrieve list of experiments
    function refreshExperiments(){
      RestService.getExperiments()
        .success(function(response){
          $scope.experiments=response.values;
          $scope.displayExperiments=[].concat($scope.experiments);
          })
        .error(function(response){
          $scope.errorText="Could not retrieve experiments list.";
          });
    }

    refreshExperiments();

    /** Opens the edit screen for an experiment
     * uses the currently selected experiment
     */
    $scope.editRow=function(){
      if ($scope.currentExperiment) {
        $location.path('/experiment/addedit/' + $scope.currentExperiment.id);
        $scope.startTour();
      }
    };

    // Deletes an experiment from database
    $scope.removeItem=function(row){
      var confirmed = confirm('Delete \'' + row.name + '\'?');
      if (confirmed) {
        RestService.deleteExperiment(row.id)
          .success(function (response) {
            for (var i = 0; i < $scope.experiments.length; i++)
              if ($scope.experiments[i].id === row.id) {
                $scope.experiments.splice(i, 1);
                break;
              }
          })
          .error(function (response) {
            console.log('Error: ' + response);
          });
      }
    };

    // publish an experiment
    $scope.publish = function(experiment) {
      var confirmed = confirm('Publish '+experiment.name+'?\n' +
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

    function gotoExperimentDetails(){
      $location.url('/experiment/' + $rootScope.currentExperiment.id);
    }

    // fired when table rows are selected
    $scope.$watch('displayExperiments', function(newVal, oldVal) {
      if(newVal) {
        if (waitingForSecondClick) {
          waitingForSecondClick = false;
          gotoExperimentDetails();
        } else {
          waitingForSecondClick = true;
          $timeout(function(){
            waitingForSecondClick = false;
          }, 600);
          var list = newVal.filter(function (item) {
            return item.isSelected;
          });
          if (list.length == 0)
            $rootScope.currentExperiment = null;
          else
            $rootScope.currentExperiment = list[0];
        }
      }
    }, true);

    $scope.dismiss=function(type){
    	if(type==='info'){
    		$scope.infoText=null;
    	}
    	else if(type==='error')
    		$scope.errorText=null;
    };

      //=== Tour Settings

      $scope.startTour = function () {
        $scope.tourConfig = TourConstants.cleanTourConfig(TourConstants.experimentListTour);
        if ($scope.tourConfig.length >= 1)
          $scope.startJoyRide = true;
      };

}]);
