'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:AddPlateSetCtrl
 * @description
 * # AddPlateSetCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('AddPlateSetCtrl', function ($scope, $window, $modal, PlateTypeModel) {
    $scope.selected ={};
    $scope.step = 'step1';

    $scope.changeStep = function(stepId) {
      $scope.step=stepId;
    };

    $scope.plateTypes = PlateTypeModel.query(function done(){
      $scope.selected.plateType = $scope.plateTypes[0];
    });


    /** Go back function */
    $scope.goBack = function(){
      $window.history.back();
    };

    /** Open order more plate types info popup */
    $scope.openOrderMoreModal = function(plateType){
      var modalInstance = $modal.open({
        size:'lg',
        templateUrl:'views/plate-mgmt/ordermoremodal.html',
        controller: 'OrderMorePlateTypesCtrl',
        resolve: {
          plateType: function(){return plateType;}
        }
      });
      //modalInstance.result.then(function(){;});
    };

  })
  .controller('OrderMorePlateTypesCtrl', function($scope, plateType){
    $scope.plateType = plateType;
  });
