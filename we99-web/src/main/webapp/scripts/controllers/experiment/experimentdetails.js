'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:ExperimentDetailsCtrl
 * @description
 * # ExperimentDetailsCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('ExperimentDetailsCtrl', ['$scope', '$routeParams', 'SelectedExperimentSvc','$modal',
    function ($scope, $routeParams, SelectedExperimentSvc, $modal) {
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
          for(var i=0;i<plates.length;i++){
            var plate=plates[i];
            plate.hasResults=false;
            SelectedExperimentSvc.getResults(plate.id).then(
              function(){plate.hasResults=true;},
              function(){plate.hasResults=false;});
          };
          $scope.plates = plates;
          $scope.displayedPlates = plates;
          $scope.show.addPlateForm = false;
        });
      }

      $scope.addResult = function(plate) {
        var modalInstance = $modal.open({
          backdrop: true,
          size: 'lg',
          templateUrl: 'views/plate-analysis/importresults.html',
          controller: 'ImportResultsCtrl',
          resolve: {
            experiment: function () {
              return $scope.experiment;
            },
            plate: function () {
              return plate;
            }

          }
        });
        modalInstance.result.then(function (returnVal) {
          $scope.refreshPlates(); // Refreshes plate  when add result screen closed
        });
      };

    }]);
