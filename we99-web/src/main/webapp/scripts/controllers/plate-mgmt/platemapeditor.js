'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:PlateMapEditorCtrl
 * @description
 * # PlateMapEditorCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('PlateMapEditorCtrl', function ($scope, $modal,$log, PlateMapModel) {
    $scope.displayPlateMaps = [];
    $scope.plateMaps = [];

    /* Actions */

    /**
     * Removes plate map from system
     * @param row
     */
    $scope.removePlateMap=function(row){
      PlateMapModel.delete({ id: row.id },function done(resp){
        $log.info('delete response:'+resp);
        //remove deleted map from our backing list
        for(var i in $scope.plateMaps){
          if($scope.plateMaps[i].id===row.id)
            $scope.plateMaps.splice(i,1);
        }
      });
    }

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
      $scope.plateMaps = PlateMapModel.listPlateMaps(function done(plateMapList){
        $scope.displayPlateMaps = [].concat(plateMapList);
        console.log($scope.plateMaps);
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
