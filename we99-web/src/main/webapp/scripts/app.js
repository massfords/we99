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
    'ngTouch',
    'ui.bootstrap',
    'smart-table'
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
        controller: 'PlateTypeEditorCtrl'
      })
      .when('/heatmap', {
        templateUrl: 'views/plate-analysis/heatmap.html',
        controller: 'HeatmapCtrl'
      })
      .when('/experiment', {
        templateUrl: 'views/experiment/experimentList.html',
        controller: 'ExperimentListCtrl'
      })
      .when('/experiment/create', {
        templateUrl: 'views/experiment/create.html',
        controller: 'ExperimentCreateCtrl'
      })
      .otherwise({
        redirectTo: '/experiment'
      });
  });
