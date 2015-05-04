'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:PlateMapEditorCtrl
 * @description
 * # PlateMapEditorCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('PlateMapEditorCtrl', function ($scope, $modal,$log, PlateMapModel,TourConstants) {
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


      //when modal closes...
      modalInstance.result.then(function (modalMessage) {
        $scope.modalSuccessMsg=modalMessage;
        refreshPlateMapsList(); // Refreshes plate types when add screen closed
      }, function () {
        $log.log('Modal dismissed at: ' + new Date());
      });

    };

    /* HELPERS */
    function refreshPlateMapsList (){
      $scope.plateMaps = PlateMapModel.listPlateMaps(function done(plateMapList){
        $scope.displayPlateMaps = [].concat(plateMapList);
        $log.info($scope.plateMaps);
      });
    }

    $scope.modalSuccessMsg=null;
    $scope.closeAlert=function(){
      $scope.modalSuccessMsg=null;
    }

    /* RUN ON LOAD */
    refreshPlateMapsList();

    //=== Tour Settings ===

    $scope.startTour=function(){
      $scope.startJoyRide=true;
    };

    $scope.tourConfig=TourConstants.plateMapTour;

  })
  .controller('AddPlateMapCtrl', function($scope,RestURLs,$modalInstance,$log,$upload){
      $scope.name = null;
      $scope.description = null;
      $scope.plateMapFile = null;

    /** Close without adding */
    $scope.cancel = function () {
      $log.log('cancel clicked');
      $modalInstance.dismiss();
    };

    $scope.addPlateMap = function(){
      if(!$scope.name){
        $scope.errorTxt="Missing name field for plate map."
        return;
      }
      if(!$scope.description){
        $scope.errorTxt="Missing description field for plate map."
        return;
      }
      if(!$scope.plateMapFile){
        $scope.errorTxt="Missing csv file for plate map."
        return;
      }

      $scope.upload($scope.plateMapFile);


    };

    $scope.errorTxt=null;
    $scope.closeErrorAlert=function(){
      $scope.errorTxt=null;
    }

    $scope.upload = function (files) {
      if (files && files.length) {
        for (var i = 0; i < files.length; i++) {
          var file = files[i];
          $upload.upload({
            url: RestURLs.plateMap,
            method: "POST",
            file: file,
            fields: {name: $scope.name, description: $scope.description}
          }).progress(function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            $log.log('progress: ' + progressPercentage + '% ' +
            evt.config.file.name);
          }).success(function () {
            $modalInstance.close('New plate map uploaded!');
          }).error(function () {
            $scope.errorTxt='Error uploading plate map csv. Parse failed.';
          })

          ;
        }
      }
    };
  });
