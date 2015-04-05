'use strict';

/**
 * @ngdoc directive
 * @name we99App.directive:wePair
 * @description
 * # wePair
 */
angular.module('we99App')
  .directive('wePair', function () {
    return {
      template: '<div><span class="name">{{name}}:</span> <span class="value">{{value}}</span></div>',
      restrict: 'E',
      scope: {
        name: '@',
        value: '@'
      }
    };
  });
