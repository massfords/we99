'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:AddPlateTypeCtrl
 * @description Controller for Add Plate Template Wizard
 * # AddPlateTypeCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('AddPlateTypeCtrl', ['$scope', '$modalInstance', 'RestService', 'option',
    function ($scope, $modalInstance, RestService, option) {
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
      if (!option.isCustom){
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
        RestService.plateType.put(reqBody); // server takes put for creation
        $modalInstance.close();
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
          errors.push("Plate type needs a name");
        }
        // Dimensions
        if (plateTypeValues.rows <= 0 && plateTypeValues.cols <= 0) {
          errors.push("Invalid plate type dimensions");
        }

        return errors.length === 0;
      }
    }]);
