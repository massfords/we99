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
  .controller('AddPlateCtrl', function($scope, SelectedExperimentSvc, PlateTypeModel, PlateMapModel, kCompoundUOM){
      $scope.selectedPlateType = null;
      $scope.plateMaps = null;
      $scope.experiment = SelectedExperimentSvc.selected();
      $scope.plateTypes = PlateTypeModel.query(function done(data){
        $scope.plateTypes = data;
      });

      $scope.plateMapsForPlateType = function() {
        var maxRows = $scope.selectedPlateType.dim.rows,
            maxCols = $scope.selectedPlateType.dim.cols;
        $scope.plateMaps = PlateMapModel.listPlateMaps({maxRows:maxRows,maxCols:maxCols},
          function done(data) {
            $scope.plateMaps = data;
          });
      };


    /** Row Class for Label Table
     *  You can set the defaults in this class as well
     * @param label the well label
     * @param type the well type
     * @constructor
     */
      function LabelTableRow(label, type) {
        this.label = label;
        this.type = type;
        this.count = 1;
        this.replicates = 1;
        this.compound = null;
        this.initialDoseAmt = 100;
        this.initialDoseUOM = kCompoundUOM.uM;
        this.dilutionFactor = 1;
      }
  });
