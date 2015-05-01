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
          size: 'lg',
          templateUrl: 'views/plate-mgmt/importresults.html',
          controller: 'ImportResultsCtrl',
          resolve: {
            experiment: function () {
              return SelectedExperimentSvc.getSelected();
            }
          }
        });
        modalInstance.result.then(function (returnVal) {
          $scope.refreshPlates(); // Refreshes plate  when bulk results modal closed
        });

      };

      //=== Tour Settings ===

      $scope.startTour=function(){
        $scope.startJoyRide=true;
      };

      $scope.tourConfig=[
        {
          type: "element",
          selector: "#primary-details",
          heading: "Experiment Details",
          text: "This is the Experiment Details page. Details for the specific experiment are listed here.",
          placement: "top",
          scroll: true
        }
        ,
        {
          type: "element",
          selector: "#plateTable",
          heading: "Experiment Details",
          text: "The table shows the list of plates associated with this experiment",
          placement: "top",
          scroll: true
        }
        ,
        {
          type: "element",
          selector: "#addPlateBtn",
          heading: "Experiment Details",
          text: "Use to button to add a single new plate with a list of compounds and dosage concentrations.",
          placement: "top",
          scroll: true
        }
        ,{
          type: "element",
          selector: "#bulkPlateBtn",
          heading: "Experiment Details",
          text: "Use this button to create a bulk set of plates or upload a bulk set of results.",
          placement: "top",
          scroll: true,
          attachToBody:true
        }

        ,{
          type: "element",
          selector: ".glyphicon-remove-circle:first",
          heading: "Experiment Details",
          text: "Click the delete button to remove a plate from the experiment.",
          placement: "top",
          scroll: true
        }

      ];

    }]);
