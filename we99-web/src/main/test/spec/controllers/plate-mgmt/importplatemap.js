'use strict';

describe('Controller: PlateMgmtImportplatemapCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var PlateMgmtImportplatemapCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    PlateMgmtImportplatemapCtrl = $controller('PlateMgmtImportplatemapCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
