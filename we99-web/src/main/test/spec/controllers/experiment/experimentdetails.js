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
    },
    samplePlates = [
      {
        "id": 1,
        "name": "plate 0 for exp 1",
        "experiment": {},
        "barcode": "abc0",
        "labels": [],
        "wells": [],
        "plateType": {}
      },
      {
        "id": 2,
        "name": "plate 1 for exp 1",
        "experiment": {},
        "barcode": "abc1",
        "labels": [],
        "wells": [],
        "plateType": {
          "id": 1,
          "name": "Corning-3788",
          "description": "Clear high-grade polystyrene for research diagnostic assays Formulated for uniform binding\\",
          "dim": {
            "rows": 8,
            "cols": 12
          },
          "manufacturer": "Corning",
          "orderLink": "http://www.coleparmer.com/Product/Corning_Plate_96_Well_ps_round_100_cs_Clear/UX-01728-07"
        }
      }
    ];
  beforeEach(inject(function (_$httpBackend_) {
    $httpBackend = _$httpBackend_;
    $httpBackend.whenGET('services/rest/experiment/1').respond(sampleExperiment);
    var plateResponse = {
      "totalCount": 2,
      "page": 0,
      "pageSize": 100,
      "values": samplePlates
    };
    $httpBackend.whenGET(/services\/rest\/experiment\/1\/plates*/).respond(plateResponse);
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

  it('should have a value for scope.experiment', function () {
    $httpBackend.flush();
    expect(scope.experiment).toEqual(sampleExperiment);
  });

  it('should get the list of plates for the experiment on load', function() {
    $httpBackend.flush();
    expect(scope.plates).toEqual(samplePlates);
  });

});
