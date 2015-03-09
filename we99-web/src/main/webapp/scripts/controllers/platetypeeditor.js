'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:TemplateeditorCtrl
 * @description
 * # TemplateeditorCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('PlateTypeEditorCtrl', ['$scope', 'RestService', function ($scope, RestService) {
    $scope.title = "Select a Plate Template";
    RestService.plateType.get(function(data){
        console.log(data);
    });
  }]);
