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
      .when('/plate-editor', {
        templateUrl: 'views/plate-mgmt/plateeditor.html',
        controller: 'PlateEditorCtrl'
      })
      .when('/plate-type-editor', {
        templateUrl: 'views/plate-mgmt/platetypeeditor.html',
        controller: 'TemplateEditorCtrl'
      })
      .when('/heatmap', {
        templateUrl: 'views/plate-analysis/heatmap.html',
        controller: 'HeatmapCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
