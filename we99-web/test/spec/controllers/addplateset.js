'use strict';

describe('Controller: AddPlateSetCtrl', function () {

  // load the controller's module
  beforeEach(module('we99WebApp'));

  var AddplatesetCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AddplatesetCtrl = $controller('AddPlateSetCtrl', {
      $scope: scope
    });
  }));

  //it('should attach a list of awesomeThings to the scope', function () {
  //  expect(scope.awesomeThings.length).toBe(3);
  //});
});
