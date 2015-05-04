'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:CompoundsCtrl
 * @description
 * # CompoundsCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('CompoundsCtrl', function ($scope, $log,$upload,RestURLs,RestService,$modal,TourConstants) {


    //max compounds to display
    $scope.maxCompounds=10;

    $scope.compoundList=[];
    $scope.searchFilter="";

    $scope.$watch('searchFilter', function () {
      refreshCompounds();
    });


    $scope.importCompounds=function(){
      var modalInstance = $modal.open({
        backdrop: true,
        size: 'md',
        templateUrl: 'views/admin/importcompounds.html',
        controller: 'ImportCompoundsCtrl'
      });
      modalInstance.result.then(function (returnVal) {
        refreshCompounds();
      });

    };

    /**
     * Refreshes the list of available compounds, should only retrieve up to a max of "maxCompounds"
     */
    function refreshCompounds(){
      RestService.getCompounds($scope.searchFilter,$scope.maxCompounds).success(function(resp){
        $log.info('# of compounds returned:'+resp.values.length);
        $scope.compoundList=resp.values;
      });

    };

    refreshCompounds();

    //=== Tour Settings ===

    $scope.startTour=function(){
      $scope.startJoyRide=true;
    };

    $scope.tourConfig=TourConstants.compoundTour;

  });

  /// Modal section ///

    /**
     * @ngdoc function
     * @name we99App.controller:ImportCompoundsCtrl
     * @description
     * # ImportCompoundsCtrl
     * Controller of the we99App
     */
    angular.module('we99App')
      .controller('ImportCompoundsCtrl', function ($scope,$upload,RestURLs,RestService,$modalInstance,$log) {

    $scope.$watch('files', function () {
      $scope.upload($scope.files);
    });

    $scope.upload = function (files) {
      if (files && files.length) {
        for (var i = 0; i < files.length; i++) {
          var file = files[i];
          $upload.upload({
            url: RestURLs.compoundUpload,
            method: "POST",
            file: file
          }).progress(function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            $log.log('progress: ' + progressPercentage + '% ' +
            evt.config.file.name);
          }).success(function (resp) {
            $scope.uploadOutput='Success! '+resp+" compound(s).";
          }).error(function () {
            $scope.uploadOutput='Error uploading compound csv.';
          })

          ;
        }
      }
    };

        $scope.dismiss = function () {
          $modalInstance.dismiss('cancel');
        };

  });
