'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:ExperimentDetailsCtrl
 * @description
 * # ExperimentDetailsCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('ExperimentDetailsCtrl', ['$scope', '$routeParams', '$modal', 'SelectedExperimentSvc','TourConstants',
    function ($scope, $routeParams, $modal, SelectedExperimentSvc,TourConstants) {
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

      // Deletes a plate from the assay
      $scope.removePlate = function(plate) {
        var confirmed = confirm('Delete \''+ plate.name +'\'?');
        if (confirmed) {
          SelectedExperimentSvc.removePlate(plate.id).then(function () {
            console.log('plate ' + plate.id + ' was  removed');
            $scope.refreshPlates();
          });
        }
      };

      // Gets the list of assay plates from the server
      $scope.refreshPlates = function(){
        SelectedExperimentSvc.getPlates($scope.experiment.id).then(function (plates){
          $scope.plates = plates;
          $scope.displayedPlates = plates;
          $scope.show.addPlateForm = false;
        });
      }

      // opens the add plate map modal
      $scope.openAddPlateModal = function(){
        var modalInstance = $modal.open({
          templateUrl: 'views/plate-mgmt/addplate.html',
          controller: 'AddPlateCtrl',
          size: 'lg'
        });
        modalInstance.result.then(function(){
          $scope.refreshPlates();
        });
      };

      $scope.openAddWCompoundCsvModal = function(){
        var modalInstance = $modal.open({
          templateUrl: 'views/plate-mgmt/addplate-w-compoundfile.html',
          controller: 'AddPlateCtrl',
          size: 'lg'
        });
        modalInstance.result.then(function(){
          $scope.refreshPlates();
        });
      };

      $scope.openAddFullMontyCsvModal = function(){
        var modalInstance = $modal.open({
          templateUrl: 'views/plate-mgmt/addplate-w-results-from-file.html',
          controller: 'AddPlateCtrl',
          size: 'lg'
        });
        modalInstance.result.then(function(){
          $scope.refreshPlates();
        });
      };

      $scope.openBulkResultsModal=function(){
        var modalInstance = $modal.open({
          backdrop: true,
          size: 'md',
          templateUrl: 'views/plate-mgmt/importresults.html',
          controller: 'ImportResultsCtrl',
          resolve: {
            experiment: function () {
              return SelectedExperimentSvc.getSelected();
            }, // set plate to null for bulk results upload
            plate: function () {
              return null;
            }
          }
        });
        modalInstance.result.then(function (returnVal) {
          $scope.refreshPlates(); // Refreshes plate  when bulk results modal closed
        });

      };

      $scope.uploadSinglePlateResults=function(plate){
        var modalInstance = $modal.open({
          backdrop: true,
          size: 'md',
          templateUrl: 'views/plate-mgmt/importresults.html',
          controller: 'ImportResultsCtrl',
          resolve: {
            experiment: function () {
              return SelectedExperimentSvc.getSelected();
            }, // set plate to null for bulk results upload
            plate: function () {
              return plate;
            }
          }
        });
        modalInstance.result.then(function (returnVal) {
          $scope.refreshPlates(); // Refreshes plate  when bulk results modal closed
        });

      };



      //=== Tour Settings ===

      $scope.startTour=function(){
        $scope.tourConfig=TourConstants.cleanTourConfig(TourConstants.experimentDetailsTour);
        if($scope.tourConfig.length>=1)
          $scope.startJoyRide=true;
      };


    }]);
