'use strict';

describe('Controller: ExperimentDetailsCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var ExperimentDetailsCtrl,
    scope,
    routeParams,
    $httpBackend;

  // mock http calls
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
    $httpBackend.whenGET('services/rest/experiment/1').respond(sampleExperiment);
  }));

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    routeParams = {experimentId: 1};
    scope = $rootScope.$new();
    ExperimentDetailsCtrl = $controller('ExperimentDetailsCtrl', {
      $scope: scope,
      $routeParams: routeParams
    });
  }));

  it('have a value for scope.experiment', function () {
    $httpBackend.flush();
    expect(scope.experiment).not.toBeNull();
  });
});
