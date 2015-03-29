'use strict';

describe('Service: SelectedExperimentSvc', function () {

  // load the service's module
  beforeEach(module('we99WebApp'));

  // instantiate service
  var ExperimentService;
  beforeEach(inject(function (_ExperimentService_) {
    ExperimentService = _ExperimentService_;
  }));

  it('should do something', function () {
    expect(!!ExperimentService).toBe(true);
  });

});
