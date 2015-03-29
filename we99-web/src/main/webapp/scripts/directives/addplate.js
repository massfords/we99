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
      link: function postLink(scope, element, attrs) {
        element.text('wire test');
      }
    };
  })
  .controller('AddPlateCtrl', function($scope, SelectedExperimentSvc, PlateTypeModel){
      $scope.selectedPlateType = null;
      $scope.plateMaps = null;
      $scope.experiment = SelectedExperimentSvc.selected();
      $scope.plateTypes = PlateTypeModel.query(function done(data){
        $scope.plateTypes = data;
      });

      $scope.plateMapsForPlateType = function() {
        scope.selectedPlateType
      }
  });
