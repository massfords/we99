'use strict';

describe('Service: RestService', function () {

  // load the service's module
  beforeEach(module('we99App'));

  // instantiate service
  var restService;
  beforeEach(inject(function (_RestService_) {
    restService = _RestService_;
  }));

  it('should be automatically defined as injected factory', function () {
    expect(!!restService).toBe(true);
  });
});
