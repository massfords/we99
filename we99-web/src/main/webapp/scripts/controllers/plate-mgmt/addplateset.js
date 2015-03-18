'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:AddPlateSetCtrl
 * @description
 * # AddPlateSetCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('AddPlateSetCtrl', function ($scope, $window, PlateTypeModel) {
    $scope.selected ={};
    $scope.step = 'step2';

    $scope.changeStep = function(stepId) {
      console.log("CLicked " + stepId);
      $scope.step=stepId;
    };

    $scope.plateTypes = PlateTypeModel.query(function done(){
      $scope.selected.plateType = $scope.plateTypes[0];
    });


    /** Go back function */
    $scope.goBack = function(){
      $window.history.back();
    };

  });
