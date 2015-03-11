'use strict';

describe('Controller: AddplatetemplatectrlCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var AddplatetemplatectrlCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AddplatetemplatectrlCtrl = $controller('AddplatetemplatectrlCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
