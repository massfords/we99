'use strict';

describe('Controller: PlateeditorCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var PlateeditorCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    PlateeditorCtrl = $controller('PlateeditorCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
