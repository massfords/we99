'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:TemplateeditorCtrl
 * @description
 * # TemplateeditorCtrl
 * Controller of the we99App
 */
angular.module('we99App')
    .controller('PlateTypeEditorCtrl', ['$scope','$modal','RestService',
        function ($scope,$modal,RestService) {
          var kNewPlateTypeOptions = [
            // Add remove static option 'templates' here
            {name: '6-Well',    rows: 2, cols:3},
            {name: '24-Well',   rows: 4, cols:6},
            {name: '96-Well',   rows: 8, cols:12},
            {name: '384-Well',  rows: 16, cols:24}];

          /* Models */
          $scope.plateTypes = null;
          $scope.title = "Select a Plate Type";
          $scope.showAddTypeOptions = false; // 'Add' dropdown

          /* Actions */
          /** Modal popover showing addPlate Type Wizrad
           * @param option optional param for pre-populating rows or columns
           */
          $scope.showAddPlateTypeWizard = function(option) {
              if (!option) {
                  option = {name:'custom', isCustom:true};
              }
              var modalInstance = $modal.open({
                  backdrop: true,
                  size:'lg',
                  templateUrl: 'views/plate-mgmt/add-plate-type.html',
                  controller: 'AddPlateTypeCtrl',
                  resolve:{
                      option: option
                  }
              });
              modalInstance.result.then(function(returnVal){
                  refreshPlateTypesList();
              });
          };

          /** Action to create a new plate */
          $scope.getPlateTypeOptions= function(){
              return kNewPlateTypeOptions;
          };

          /* HELPERS */
          function refreshPlateTypesList (){
              $scope.plateTypes = RestService.plateType.query(function done(){
                  console.log("Plate Types Loaded");
                  console.log($scope.plateTypes);
              });
          }

          /* RUN ON LOAD */
          refreshPlateTypesList();
      }]);