'use strict';

describe('Controller: PlatetypeeditorCtrl', function () {

  // load the controller's module
  beforeEach(module('mainApp'));

  var PlatetypeeditorCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    PlatetypeeditorCtrl = $controller('PlatetypeeditorCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
