'use strict';

describe('Service: RestService', function () {

  // load the service's module
  beforeEach(module('we99App'));

  // instantiate service
  var restService;
  beforeEach(inject(function (_RestService_) {
    restService = _RestService_;
  }));

  it('should do something', function () {
    expect(!!restService).toBe(true);
  });

});
