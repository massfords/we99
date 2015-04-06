'use strict';

describe('Controller: ExperimentCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var ExperimentListCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ExperimentListCtrl = $controller('ExperimentListCtrl', {
      $scope: scope
    });
  }));
});
