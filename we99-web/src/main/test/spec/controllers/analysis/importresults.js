'use strict';

describe('Controller: AnalysisImportresultsCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var AnalysisImportresultsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AnalysisImportresultsCtrl = $controller('AnalysisImportresultsCtrl', {
      $scope: scope
    });
  }));

  //it('should attach a list of awesomeThings to the scope', function () {
  //  expect(scope.awesomeThings.length).toBe(3);
  //});
});
