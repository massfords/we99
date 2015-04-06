'use strict';

describe('Directive: wePair', function () {

  // load the directive's module
  beforeEach(module('we99App'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<we-pair></we-pair>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the wePair directive');
  }));
});
