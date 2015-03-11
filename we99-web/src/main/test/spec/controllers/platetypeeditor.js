'use strict';

describe('Controller: PlateTypeEditorCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var PlateTypeEditorCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    PlateTypeEditorCtrl = $controller('PlateTypeEditorCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.getPlateTypeOptions().length).toBeGreaterThan(2);
  });
});
