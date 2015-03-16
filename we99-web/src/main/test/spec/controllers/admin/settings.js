'use strict';

describe('Controller: AdminSettingsCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var AdminSettingsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminSettingsCtrl = $controller('AdminSettingsCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
