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
    SelectedExperimentSvc.setSelectedById($routeParams.experimentId);
    $scope.experiment = SelectedExperimentSvc.selected();
  }]);
