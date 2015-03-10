'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:AddPlateTypeCtrl
 * @description Controller for Add Plate Template Wizard
 * # AddPlateTypeCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('AddPlateTypeCtrl', ['$scope', '$modalInstance', 'RestService',
    function ($scope, $modalInstance, RestService) {
        $scope.option = null;
        $scope.plateType = {
            id: 'TBD',
            name: '',
            description:'',
            rows:null,
            cols:null,
            manufacturer:'',
            material:'',
            orderingLink:'', // link
            orderingNotes:''
        };

        /** Close without adding */
        $scope.cancel = function(){
            console.log('cancel clicked');
            $modalInstance.dismiss();
        };

        /** Add type and close modal */
        $scope.addPlateType = function(){
            console.log('add clicked');
            preVerification();
            console.log("Requesting new PlateType:");
            var reqBody = makePostObject();
            console.log(reqBody);
            RestService.plateType.put(reqBody); // server takes put for creation
            $modalInstance.close();
        };

        /** Gets scope variables and produces an object to send in post body */
        function makePostObject() {
            var model = $scope.plateType,
                postObj = {
                    name: model.name,
                    description: model.description,
                    manufacturer: model.manufacturer,
                    material: model.material,
                    link: model.orderingLink,
                    //orderingNotes: model.orderingNotes,
                    dim: {
                        rows: model.rows,
                        cols: model.cols
                    }
                };
            return postObj;
        }
        /** Verification before sending to server */
        function preVerification() {
            return true;
        }
  }]);
