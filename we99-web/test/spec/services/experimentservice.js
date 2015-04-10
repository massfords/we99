'use strict';

describe('Service: SelectedExperimentSvc', function () {

  // load the service's module
  beforeEach(module('we99WebApp'));

  // instantiate service
  var ExperimentService;
  beforeEach(inject(function (_SelectedExperimentSvc_) {
    SelectedExperimentSvc = _SelectedExperimentSvc_;
  }));

  it('should do something', function () {
    expect(!!SelectedExperimentSvc).toBe(true);
  });

});
