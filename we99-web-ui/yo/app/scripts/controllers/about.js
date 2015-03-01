'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('AboutCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
