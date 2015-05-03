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

    //retrieve list of assays
    function refreshExperiments(){
      RestService.getExperiments()
        .success(function(response){
          $scope.experiments=response.values;
          $scope.displayExperiments=[].concat($scope.experiments);
          })
        .error(function(){
          $scope.errorText="Could not retrieve assays list.";
          });
    }
    refreshExperiments();

    $scope.alerts = []; // alerts object {type:..., msg:...}
    $scope.closeAlert = function(index) {
      $scope.alerts.splice(index,1);
    };

    function addAlert(type, msg) {
      $scope.alerts.push({type:type, msg:msg});
    }

    /** Opens the edit screen for an experimental assay
     * uses the currently selected experimental assay
     */
    $scope.editRow=function(){
      if ($scope.currentExperiment) {
        $location.path('/experiment/addedit/' + $scope.currentExperiment.id);
      }
    };

    // Deletes an assay from database
    $scope.removeItem=function(row){
      var name = row.name,
          confirmed = confirm('Delete \'' + name + '\'?');
      if (confirmed) {
        RestService.deleteExperiment(row.id)
          .success(function () {
            for (var i = 0; i < $scope.experiments.length; i++)
              if ($scope.experiments[i].id === row.id) {
                $scope.experiments.splice(i, 1);
                addAlert('danger', "Deleted assay: " + name + "...");
                break;
              }
          })
          .error(function (response) {
            console.log('Error: ' + response);
          });
      }
    };

    // publish an assay
    $scope.publish = function(experiment) {
      var confirmed = confirm('Publish '+experiment.name+'?\n' +
                              'This will lock down the assay data.');
      if (confirmed) {
        experiment.isSelected = false;
        RestService.publishExperiment(experiment.id).then(function success() {
          experiment.status = 'PUBLISHED';
          addAlert('success', "Published " + experiment.name + ". Good work!");
        }, function fail(err) {
          console.error(err);
          alert('Could not publish.');
        });
      }
    };

    //function gotoExperimentDetails(){
    //  $location.url('/experiment/' + $rootScope.currentExperiment.id);
    //}

    // fired when table rows are selected
    $scope.$watch('displayExperiments', function(newVal, oldVal) {
      if(newVal) {
          var list = newVal.filter(function (item) {
            return item.isSelected;
          });
          if (list.length == 0)
            $rootScope.currentExperiment = null;
          else
            $rootScope.currentExperiment = list[0];
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
