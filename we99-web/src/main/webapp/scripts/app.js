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
    'smart-table',
    'angularFileUpload',
    'ngJoyRide',
    "checklist-model"
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      // ##################
      // Plate Management
      // ##################
      .when('/plate-type-editor', {
        templateUrl: 'views/plate-mgmt/platetypeeditor.html',
        controller: 'PlateTypeEditorCtrl'
      })
      .when('/plate-map-editor', {
        templateUrl: 'views/plate-mgmt/platemapeditor.html',
        controller: 'PlateMapEditorCtrl'
      })
      // ###################
      // Plate Results
      // ###################
      .when('/analysis/importResults', {
        templateUrl: 'views/plate-analysis/importresults.html',
        controller: 'ImportResultsCtrl'
      })
      .when('/heatmap', {
        templateUrl: 'views/plate-analysis/heatmap.html',
        controller: 'HeatmapCtrl'
      })
      .when('/omnimap', {
        templateUrl: 'views/plate-analysis/omnimap.html',
        controller: 'OmniMapCtrl'
      })
      .when('/wellqc', {
        templateUrl: 'views/plate-analysis/wellqc.html',
        controller: 'WellQcCntrl'
      })
      .when('/doseresponse', {
        templateUrl: 'views/plate-analysis/doseresponse.html',
        controller: 'DoseResponseCntrl'
      })
      .when('/compoundresults', {
        templateUrl: 'views/plate-analysis/compoundresult.html',
        controller: 'CompoundResultsCntrl'
      })
      // #####################
      // Experiment Management
      // #####################
      .when('/experiment', {
        templateUrl: 'views/experiment/experimentList.html',
        controller: 'ExperimentListCtrl'
      })
      .when('/experiment/addedit/:addeditId', {
        templateUrl: 'views/experiment/createExperiment.html',
        controller: 'ExperimentCreateCtrl'
      })
      .when('/experiment/:experimentId', {
        templateUrl:"views/experiment/experimentdetails.html",
        controller:'ExperimentDetailsCtrl'
      })
      .when('/experiment/:experimentId/add-plate', {
        templateUrl: 'views/plate-mgmt/addplate.html',
        controller: 'AddPlateCtrl'
      })
      // #####################
      // Admin
      // #####################
      .when('/admin/settings', {
        templateUrl: 'views/admin/settings.html',
        controller: 'AdminSettingsCtrl'
      })
      .when('/admin/compounds', {
        templateUrl: 'views/admin/compounds.html',
        controller: 'CompoundsCtrl'
      })
      // #####################
      // Otherwise
      // #####################
      .otherwise({
        redirectTo: '/experiment'
      });

//})
///** Intercept HTTP 401 Forbidden*/
//.factory('Interceptor401', function(){
//  var interceptor = {
//    reponse: function(response) {
//      if (response.status === 401) {
//        console.log("Response 401");
//      }
//      return response;
//    },
//    responseError: function(rejection) {
//      if (rejection.status === 401) {
//        console.log("Response Error 401", rejection);
//        $location.path('login').search('returnTo', login);
//      }
//    }
//  };
//  return interceptor;
//})
//.config(function($httpProvider) {
//  $httpProvider.interceptors.push('Interceptor401');

  });
