'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('MainCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
