'use strict';

describe('Service: SelectedExperimentSvc', function () {

  // load the service's module
  beforeEach(module('we99App'));

  // instantiate service
  var ExperimentService,
      RestService,
      $httpBackend;

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
    $httpBackend.whenGET('experiment/1').respond(sampleExperiment);
  }));
  beforeEach(inject(function (_ExperimentService_, _RestService_, _$httpBackend_) {
    ExperimentService = _ExperimentService_;
    RestService = _RestService_;
  }));
  afterEach(inject(function (_ExperimentService_) {
    ExperimentService.selected() = null;
  }));

  it('should do something', function () {
    expect(!!ExperimentService).toBe(true);
  });

  it('should get call experiment by id', function(){
    spyOn(RestService, 'getExperimentById');
    ExperimentService.setSelectedById(1);
    expect(RestService.getExperimentById.calls.count()).toEqual(1);
    expect(ExperimentService.selected).toEqual(sampleExperiment);
  })

  it('should clear selection', function(){
    ExperimentService.clearSelection();
    expect(ExperimentService.selected).toBeNull();
  })

  it('should clear selection when failed http attempt', function(){
    var errorCodes = [404, 500];
    errorCodes.forEach(function(code){
      $httpBackend.whenGET('experiment/1').respond(code,'');
      ExperimentService.setSelectedById(1);
      expect(ExperimentService.selected).toBeNull();
    });
  })

});
