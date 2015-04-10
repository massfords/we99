'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:ExperimentDetailsCtrl
 * @description
 * # ExperimentDetailsCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('ExperimentDetailsCtrl', ['$scope', '$routeParams', 'SelectedExperimentSvc',
    function ($scope, $routeParams, SelectedExperimentSvc) {
      $scope.experiment = null;
      $scope.plates = null;
      $scope.show = {
        addPlateForm: false
      };

      SelectedExperimentSvc.setSelectedById($routeParams.experimentId).then(function(){
        $scope.experiment = SelectedExperimentSvc.getSelected();
        $scope.refreshPlates();
        if ($scope.plates == 0) {
          $scope.show.addPlateForm = true;
        }
      });

      // Deletes a plate from the experiment
      $scope.removePlate = function(plate) {
        SelectedExperimentSvc.removePlate(plate.id).then(function(){
          console.log('plate ' + plate.id + ' was  removed');
          $scope.refreshPlates();
        });
      };

      // Gets the list of experiment plates from the server
      $scope.refreshPlates = function(){
        SelectedExperimentSvc.getPlates($scope.experiment.id).then(function (plates){
          $scope.plates = plates;
          $scope.displayedPlates = plates;
        });
      }

    }]);
