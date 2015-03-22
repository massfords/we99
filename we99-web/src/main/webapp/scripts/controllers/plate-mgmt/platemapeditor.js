'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:PlateMapEditorCtrl
 * @description
 * # PlateMapEditorCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('PlateMapEditorCtrl', function ($scope, $modal, PlateMapModel) {
    $scope.displayPlateMaps = [];
    $scope.plateMaps = [];

    /* Actions */

    /** Modal popover showing addPlate Type Wizard
     * @param option optional param for pre-populating rows or columns
     */
    $scope.showAddPlateMap = function(option) {
      var modalInstance = $modal.open({
        backdrop: true,
        size: 'lg',
        templateUrl: 'views/plate-mgmt/addplatemap.html',
        controller: 'AddPlateMapCtrl',
        resolve: {}
      });
      modalInstance.result.then(function (returnVal) {
        refreshPlateMapsList(); // Refreshes plate types when add screen closed
      });
    };

    /* HELPERS */
    function refreshPlateMapsList (){
      $scope.plateMaps = PlateMapModel.listPlateMaps(function done(){
        $scope.displayPlateMaps = [].concat($scope.plateMaps);
      });
    }

    /* RUN ON LOAD */
    refreshPlateMapsList();
  })
  .controller('AddPlateMapCtrl', function($scope, $modalInstance){
      $scope.name = null;
      $scope.description = null;
      $scope.plateMapFile = null;

    /** Close without adding */
    $scope.cancel = function () {
      console.log('cancel clicked');
      $modalInstance.dismiss();
    };

    $scope.add = function(){
      // TODO
      alert("add() tbd...");
    };
  });
