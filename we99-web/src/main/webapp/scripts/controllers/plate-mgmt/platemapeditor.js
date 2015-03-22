'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:PlateMapEditorCtrl
 * @description
 * # PlateMapEditorCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('PlateMapEditorCtrl', function ($scope, PlateMapModel) {
    $scope.displayPlateMaps = [];
    $scope.plateMaps = PlateMapModel.listPlateMaps(function done(){
      $scope.displayPlateMaps = [].concat($scope.plateMaps);
    })


  });
