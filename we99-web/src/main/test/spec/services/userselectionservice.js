'use strict';

describe('Service: SelectedExperimentSvc', function () {

  // load the service's module
  beforeEach(module('we99App'));

  // instantiate service
  var SelectedExperimentSvc,
      RestService,
      $httpBackend,
      getExperiment1;

  var sampleExperiment = {
    "id" : 1,
    "name" : "experiment uno",
    "description" : "Experiment using the Alpha protocol",
    "labels" : [ ],
    "status" : "UNPUBLISHED",
    "created" : "2015-03-29T15:43:03.224-05:00",
    "protocol" : {
      "id" : 1,
      "name" : "Alpha"
    }
  };


  beforeEach(inject(function (_$httpBackend_) {
    $httpBackend = _$httpBackend_;
    getExperiment1 = $httpBackend.whenGET('services/rest/experiment/1')
    getExperiment1.respond(sampleExperiment);
  }));
  beforeEach(inject(function (_SelectedExperimentSvc_, _RestService_, _$httpBackend_) {
    SelectedExperimentSvc = _SelectedExperimentSvc_;
    RestService = _RestService_;
  }));
  afterEach(function () {
    SelectedExperimentSvc.setSelected(null);
  });

  it('should do something', function () {
    expect(!!SelectedExperimentSvc).toBe(true);
  });

  it('should get call experiment by id', function() {
    spyOn(RestService, 'getExperimentById').and.callThrough();
    SelectedExperimentSvc.setSelectedById(1);
    expect(RestService.getExperimentById.calls.count()).toBe(1);
  });
  it('should assign the selected when setSelectedById', function() {
    SelectedExperimentSvc.setSelectedById(1);
    $httpBackend.flush();
    expect(SelectedExperimentSvc.getSelected()).toEqual(sampleExperiment);
  });
  it('should clear selection', function(){
    SelectedExperimentSvc.clearSelection();
    expect(SelectedExperimentSvc.getSelected()).toBeNull();
  });

  it('should clear selection when failed http attempt', function(){
    var errorCodes = [404, 500];
    errorCodes.forEach(function(code){
      getExperiment1.respond(code,'');
      SelectedExperimentSvc.setSelectedById(1);
      $httpBackend.flush();
      expect(SelectedExperimentSvc.getSelected()).toBeNull();
    });
  })

});
