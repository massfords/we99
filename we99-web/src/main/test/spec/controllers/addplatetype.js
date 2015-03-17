'use strict';

describe('Controller: AddPlateTypeCtrl', function () {
// load the controller's module
  beforeEach(module('we99App'));

  var AddPlateTypeCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AddPlateTypeCtrl = $controller('AddPlateTypeCtrl', {
      $scope: scope
    });
  }));

  //it('should grab the list of plate types', function () {
  //  expect($scope.plateTypes)
  //});
});
