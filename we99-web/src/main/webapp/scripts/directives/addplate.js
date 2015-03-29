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
      templateUrl: "views/plate-mgmt/addplate.html",
      scope: {
        experimentName:"=",
        experimentId:"="
      }
      //link: function postLink(scope, element, attrs) {
      //  element.text('wire test');
      //}
    };
  })
  .controller('AddPlateCtrl', function(){

  });
