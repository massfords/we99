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

    /** Gets the valid replicates given a count
     * @param count
     * @returns An array of valid numbers
     *
     * Logic:
     * Get the integer square root
     * Check every number up to the integer square root
     * If a number can divide the count, then add that number to validValuesSmall
     *      and adds it's reciprocal to validValuesLarge
     * Reverse the second array and combine it to the first.
     * e.g., count = 10. Integer Sqrt = 3 validValues (by order retrieved) = small:[1,2] large:[10,5]
     *        return = [1,2,5,10]
     */
    $scope.computeReplicates = function(count){
      if (count < 0) {
        throw new Error("Illegal count argument");
      }

      var n = 1,
          intSqrt = Math.floor(Math.sqrt(count)),
          validValuesSmall = [],
          validValuesLarge = [],
          complement;
      while(n <= intSqrt) {
        if (count % n === 0) {
          validValuesSmall.push(n);
          complement = count / n;
          if (complement != n) {
            validValuesLarge.push(complement);
          }
        }
        ++n;
      }
      validValuesLarge.reverse();
      return validValuesSmall.concat(validValuesLarge);
    }

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
                // Only check labels with the name "lbl1". Ignore all other labels.
                if (label.name === 'lbl1') {
                  // create row if does not exist. Then increment count.
                  if (!labelTableRows.hasOwnProperty(label.value)) {
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
