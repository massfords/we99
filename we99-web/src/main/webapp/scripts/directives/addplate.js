'use strict';

/**
 * @ngdoc directive
 * @name we99App.directive:addplate
 * @description
 * # addplate
 */
angular.module('we99App')
  .directive('addplate', function () {
    return {
      restrict: 'E',
      //template:"<div>wire test</div>",
      templateUrl: 'views/plate-mgmt/addplate.html',
      scope: {},
      controller:'AddPlateCtrl'
      //link: function postLink(scope, element, attrs) {
      //  element.text('wire test');
      //}
    };
  })
  .controller('AddPlateCtrl', function($scope, kCompoundUOM, SelectedExperimentSvc, PlateTypeModel, PlateMapModel, LabelTableSvc) {
    $scope.selectedPlateType = null;
    $scope.selectedPlateMap = null;
    $scope.plateMaps = null;
    $scope.labelTable = [];
    $scope.experiment = SelectedExperimentSvc.getSelected();
    $scope.UOMOptions = Object.keys(kCompoundUOM);

    /** Grabs a list of plate types on load */
    $scope.plateTypes = PlateTypeModel.query(function done(data) {
      $scope.plateTypes = data;
      // DEFAULT VALUE
      $scope.selectedPlateType = data[0];
      $scope.plateMapsForPlateType();
    });

    /** Grabs a list of plate maps when a plate type is selected */
    $scope.plateMapsForPlateType = function () {
      var maxRows = $scope.selectedPlateType.dim.rows,
          maxCols = $scope.selectedPlateType.dim.cols;
      $scope.plateMaps = PlateMapModel.listPlateMaps({maxRows: maxRows, maxCols: maxCols},
        function done(data) {
          $scope.plateMaps = data;
          //// DEFAULT VALUE
          //$scope.selectedPlateMap = data[0];
          //$scope.makeLabelTable();
        });
    };

    /** Gets the label table when a plate map is selected */
    $scope.makeLabelTable = function() {
      $scope.labelTable = LabelTableSvc.plateMapToLabelTable($scope.selectedPlateMap);
    };

    $scope.computeReplicates = LabelTableSvc.computeReplicates;

    $scope.findCompoundMatches = function(query) {
      return LabelTableSvc.findCompoundMatches(query).then(function(data){
        return data;
      });
    };
  });
