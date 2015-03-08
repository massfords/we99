'use strict';

/**
 * @ngdoc overview
 * @name we99App
 * @description
 * # we99App
 *
 * Main module of the application.
 */
angular
  .module('we99App', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .when('/plateEditor', {
        templateUrl: 'views/plateeditor.html',
        controller: 'PlateEditorCtrl'
      })
      .when('/templateEditor', {
        templateUrl: 'views/templateeditor.html',
        controller: 'TemplateEditorCtrl'
      })
      .when('/heatmap', {
        templateUrl: 'views/heatmap.html',
        controller: 'HeatmapCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
