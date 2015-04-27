'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:ExperimentDetailsCtrl
 * @description
 * # ExperimentDetailsCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('ExperimentDetailsCtrl', ['$scope', '$routeParams', '$modal', 'SelectedExperimentSvc',
    function ($scope, $routeParams, $modal, SelectedExperimentSvc) {
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

      // Deletes a plate from the experiment
      $scope.removePlate = function(plate) {
        var confirmed = confirm('Delete \''+ plate.name +'\'?');
        if (confirmed) {
          SelectedExperimentSvc.removePlate(plate.id).then(function () {
            console.log('plate ' + plate.id + ' was  removed');
            $scope.refreshPlates();
          });
        }
      };

      // Gets the list of experiment plates from the server
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
          templateUrl: 'views/plate-mgmt/addplate-w-compounds.html',
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

    }]);
