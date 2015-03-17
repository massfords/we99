'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:AddPlateTypeCtrl
 * @description Controller for Add Plate Template Wizard
 * # AddPlateTypeCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('AddPlateTypeCtrl', ['$scope', '$modalInstance', 'PlateTypeModel', 'option',
    function ($scope, $modalInstance, PlateTypeModel, option) {
      $scope.plateType = {
        id: 'TBD',
        name: '',
        description: '',
        rows: null,
        cols: null,
        manufacturer: '',
        material: '',
        orderingLink: '', // link
        orderingNotes: ''
      };

      // handle preselected options
      if (!option.isCustom) {
        $scope.plateType.name = option.name + " Plate";
        $scope.plateType.rows = option.rows;
        $scope.plateType.cols = option.cols;
      }

      /** Close without adding */
      $scope.cancel = function () {
        console.log('cancel clicked');
        $modalInstance.dismiss();
      };

      /** Add type and close modal */
      $scope.addPlateType = function () {
        console.log('add clicked');
        preVerification($scope.plateType);
        console.log("Requesting new PlateType:");
        var reqBody = makePostObject($scope.plateType);
        console.log(reqBody);
        PlateTypeModel.create(reqBody, function done(){
          $modalInstance.close();
        }, function error(){
          $modalInstance.close();
        });
      };

      /** Gets scope variables and produces an object to send in post body */
      function makePostObject(plateType) {
        var postObj = {
                        name: plateType.name,
                        description: plateType.description,
                        manufacturer: plateType.manufacturer,
                        //material: plateType.material,
                        link: plateType.orderingLink,
                        //orderingNotes: plateType.orderingNotes,
                        dim: {
                          rows: plateType.rows,
                          cols: plateType.cols
                        }
                      };
        return postObj;
      }

      /** Runtime Verification before sending to server
       *
       * @param plateTypeValues values entered by the user for adding a plate type
       * @param errors Pass in an empty array if you want to see the errors.
       * @returns true if valid, false otherwise. Error parameter will be populated with errors
       */
      function preVerification(plateTypeValues, errors) {
        assert(Array.isArray(errors));
        // Name
        if (!$scope.plateType.name || $scope.plateType.name.length === 0) {
          if (errors) {
            errors.push("Plate type needs a name");
          } else {
            return false;
          }
        }
        // Dimensions
        if (plateTypeValues.rows <= 0 && plateTypeValues.cols <= 0) {
          if (errors) {
            errors.push("Invalid plate type dimensions");
          } else {
            return false;
          }
        }
        return !errors || errors.length === 0;
      }

    }]); // End Controller

/** Assert Warning Function */
function assert(predicate, msg) {
  if (!predicate) {
    msg = "[ASSERT ERROR]: " + msg ? msg : "Assertion Failed";
    console.error(msg);
  }
}
