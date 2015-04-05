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
        SelectedExperimentSvc.getPlates($scope.experiment.id).then(function (plates){
          $scope.plates = plates;
        });
      });
    }]);
