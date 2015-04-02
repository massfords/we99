'use strict';

/**
 * @ngdoc service
 * @name we99App.labeltableservice
 * @description
 * # LabelTableService
 * Service used for producing the label table and its features.
 */
angular.module('we99App')
  .factory('LabelTableSvc', function (_, kCompoundUOM) {
    var factory = {};

    factory.LabelTableRow = LabelTableRow;

    /** Gets the unassigned labelTableRows from a platemap object */
    factory.plateMapToLabelTable = function (plateMap) {
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
    };

    /** Gets the valid replicates given a count
     * @param count
     * @returns Array.<T> array of valid numbers
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
    factory.computeReplicates = function (count) {
      if (count < 0) {
        throw new Error("Illegal count argument");
      }

      var n = 1,
        intSqrt = Math.floor(Math.sqrt(count)),
        validValuesSmall = [],
        validValuesLarge = [],
        complement;
      while (n <= intSqrt) {
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
    };


    return factory;
  });
