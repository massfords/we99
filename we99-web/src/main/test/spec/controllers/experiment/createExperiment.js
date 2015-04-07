'use strict';

describe('Controller: ExperimentCreateCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var ExperimentCreateCtrlNew,ExperimentCreateCtrlEdit,
    scope, scopeEdit, httpBackend,location, modal, routeParams,
    expResp,protocolResp,currUserResp,allUsersResp;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, $httpBackend) {
    scope= $rootScope.$new();
    scopeEdit = $rootScope.$new();

    //basic mock of location service
    location={
      saved: "",
      path: function(loc){this.saved=loc;}
    };

    modal={};
    routeParams={addeditId: 1};

    //response mocks

    protocolResp={
      "totalCount": 5,
      "page": 0,
      "pageSize": 100,
      "values": [
        {
          "id": 1,
          "name": "Alpha"
        },
        {
          "id": 2,
          "name": "Beta"
        },
        {
          "id": 3,
          "name": "Gamma"
        },
        {
          "id": 4,
          "name": "Delta"
        },
        {
          "id": 5,
          "name": "Omega"
        }
      ]
    };
    expResp={
      "id": 1,
      "name": "experiment uno",
      "description": "Experiment using the Alpha protocol",
      "labels": [],
      "status": "UNPUBLISHED",
      "created": "2015-04-06T23:08:44.134-04:00",
      "protocol": protocolResp.values[0]
    };
    allUsersResp={
      "totalCount": 2,
      "page": 0,
      "pageSize": 100,
      "values": [
        {
          "id": 2,
          "email": "we99.2015@example",
          "firstName": "Guest",
          "lastName": "User"
        },
        {
          "id": 1,
          "email": "we99.2015@gmail.com",
          "firstName": "Test",
          "lastName": "User"

        }
      ]
    };

    currUserResp={
      "id": 1,
      "email": "we99.2015@gmail.com",
      "firstName": "Test",
      "lastName": "User"

    };


    // Setup unit test Rest Calls
    httpBackend = $httpBackend;
    httpBackend.whenGET("services/rest/experiment/1").respond(expResp);
    httpBackend.whenGET("services/rest/user/me").respond(currUserResp);
    httpBackend.whenGET("services/rest/user").respond(allUsersResp);
    httpBackend.whenGET("services/rest/protocol").respond(protocolResp);
    httpBackend.whenGET("services/rest/experiment/1/members").respond(currUserResp);

    ExperimentCreateCtrlNew = $controller('ExperimentCreateCtrl', {
      $scope: scope,
      $location: location,
      $modal: modal,
      $routeParams: {}

    });

    ExperimentCreateCtrlEdit = $controller('ExperimentCreateCtrl', {
      $scope: scopeEdit,
      $location: location,
      $modal: modal,
      $routeParams: routeParams

    });

  }));

  it('should load in edit mode if supplied experiment id', function () {

    expect(scopeEdit.editMode).toBe(true);
    expect(scopeEdit.pageTitle).toBe('Modify Experiment');

    //httpBackend.flush();
    //expect(scopeEdit.protocolValues.length).toBe(protocolResp.length);



  });

  it('should load in create mode if NOT supplied any experiment id', function () {
    expect(scope.editMode).toBe(false);
    expect(scope.pageTitle).toBe('Create New Experiment');
  });
});
