'use strict';

describe('Controller: ExperimentCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var ExperimentListCtrl,
    scope,
    httpBackend,expResp,location;

  expResp ={
    "totalCount": 3,
    "page": 0,
    "pageSize": 100,
    "values": [
      {
        "id": 2,
        "name": "experiment dos",
        "description": "Experiment using the Beta protocol",
        "labels": [],
        "status": "UNPUBLISHED",
        "created": "2015-04-06T23:08:44.380-04:00",
        "protocol": {
          "id": 2,
          "name": "Beta"
        }
      },
      {
        "id": 1,
        "name": "experiment uno",
        "description": "Experiment using the Alpha protocol",
        "labels": [],
        "status": "UNPUBLISHED",
        "created": "2015-04-06T23:08:44.134-04:00",
        "protocol": {
          "id": 1,
          "name": "Alpha"
        }
      },
      {
        "id": 3,
        "name": "experiment tres",
        "description": "Experiment using the Gamma protocol",
        "labels": [],
        "status": "UNPUBLISHED",
        "created": "2015-04-06T23:08:44.551-04:00",
        "protocol": {
          "id": 3,
          "name": "Gamma"
        }
      }
    ]
  }



  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope,$httpBackend) {
    scope = $rootScope.$new();

    // Setup unit test Rest Calls
    httpBackend = $httpBackend;
    httpBackend.whenGET("services/rest/experiment").respond(expResp);
    httpBackend.whenDELETE("services/rest/experiment/2").respond("");

    //basic mock of location service
    location={
      saved: "",
      path: function(loc){this.saved=loc;}}

    ExperimentListCtrl = $controller('ExperimentListCtrl', {
      $scope: scope,
      $location: location
    });
  }));

  it('should retrieve all assays on init', function () {
    httpBackend.flush();
    expect(scope.experiments.length).toBe(3);
    expect(scope.experiments[0].name).toBe("experiment dos");
    expect(scope.experiments[2].protocol.name).toBe("Gamma");

    expect(scope.displayExperiments.length).toBe(3);
    expect(scope.displayExperiments[0].name).toBe("experiment dos");
    expect(scope.displayExperiments[2].protocol.name).toBe("Gamma");

  });

  it('should remove assay after delete call', function () {
    httpBackend.flush();
    expect(scope.experiments.length).toBe(3);

    var targetDelete={id:2, name:'Blah blah'};

    // test confirmed delete
    spyOn(window, 'confirm').and.returnValue(true);
    scope.removeItem(targetDelete);
    httpBackend.flush();
    expect(scope.experiments.length).toBe(2);
    expect(scope.experiments[0].name).toBe("experiment uno");
    for(var i=0;i<scope.experiments.length;i++){
      expect(scope.experiments[i].id).not.toBe(2);
    }
  });

  it('should not remove assay if delete call canceled', function () {
    httpBackend.flush();
    expect(scope.experiments.length).toBe(3);

    var targetDelete={id:2, name:'Blah blah'};

    // test canceled delete
    spyOn(window, 'confirm').and.returnValue(false);
    scope.removeItem(targetDelete);
    httpBackend.verifyNoOutstandingExpectation();
    httpBackend.verifyNoOutstandingRequest();

  });

  it('should get new url path after edit call', function () {
    var targetIndex=2;
    httpBackend.flush();
    scope.currentExperiment = scope.experiments[targetIndex];
    scope.editRow();
    expect(location.saved).toBe('/experiment/addedit/'+ scope.currentExperiment.id);

  });
});
