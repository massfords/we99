'use strict';

describe('Controller: ExperimentCreateCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var ExperimentCreateCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ExperimentCreateCtrl = $controller('ExperimentCreateCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
