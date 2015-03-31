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
  .controller('AddPlateCtrl', function($scope, SelectedExperimentSvc, PlateTypeModel, PlateMapModel, LabelTableSvc) {
    $scope.selectedPlateType = null;
    $scope.selectedPlateMap = null;
    $scope.plateMaps = null;
    $scope.labelTable = [];
    $scope.experiment = SelectedExperimentSvc.getSelected();

    /** Grabs a list of plate types on load */
    $scope.plateTypes = PlateTypeModel.query(function done(data) {
      $scope.plateTypes = data;
    });

    /** Grabs a list of plate maps when a plate type is selected */
    $scope.plateMapsForPlateType = function () {
      var maxRows = $scope.selectedPlateType.dim.rows,
        maxCols = $scope.selectedPlateType.dim.cols;
      $scope.plateMaps = PlateMapModel.listPlateMaps({maxRows: maxRows, maxCols: maxCols},
        function done(data) {
          $scope.plateMaps = data;
        });
    };

    /** Gets the label table when a plate map is selected */
    $scope.makeLabelTable = function() {
      $scope.labelTable = LabelTableSvc.plateMapToLabelTable($scope.selectedPlateMap);
    };

  })

  .factory('LabelTableSvc', function(_, kCompoundUOM) {
    return {
      /** Gets the unassigned labelTableRows from a platemap object */
      plateMapToLabelTable: function(plateMap) {
        // TODO: MAKE ASYNCHRONOUS with $q and $timeout
        var labelTableRows = {};
        if (plateMap) {
          plateMap.wells.forEach(function (well) {
            if (well.labels) {
              well.labels.forEach(function (label) {
                // Only check labels with the name "lbl". Ignore all other labels.
                if (label.name === 'lbl') {
                  // create row if does not exist. Then increment count.
                  if (!labelTableRows.hasOwnProperty(label.value)) {
                    console.error(label);
                    labelTableRows[label.value] = new LabelTableRow(label.value, well.type);
                  }
                  ++(labelTableRows[label.value].count);
                }
              });
            } // endif well.labels
          });
        }
        return _.values(labelTableRows);
      },
      LabelTableRow : LabelTableRow
    };

    /** Row Class for Label Table
     *  You can set the defaults in this class as well
     * @param label the well label
     * @param type the well type
     * @constructor
     */
    function LabelTableRow(label, type) {
      if (angular.isUndefined(type)) {
        throw new TypeError("Type required to build label table row");
      }
      this.label = label;
      this.type = type;
      this.count = 0;
      this.replicates = 1;
      this.compound = null;
      this.initialDoseAmt = 100;
      this.initialDoseUOM = kCompoundUOM.uM;
      this.dilutionFactor = 1;
    }
  });
