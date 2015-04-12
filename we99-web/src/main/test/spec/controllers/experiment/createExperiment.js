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
    httpBackend.whenGET("services/rest/experiment/1/members").respond({values: [currUserResp]});



    ExperimentCreateCtrlNew = $controller('ExperimentCreateCtrl', {
      $scope: scope,
      $location: location,
      $modal: modal,
      $routeParams: {addeditId: "new"}

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

    httpBackend.flush();

    //check protocol list & users
    expect(scopeEdit.protocolValues.length).toBe(protocolResp.totalCount);
    expect(scopeEdit.assignedUsers[0]).toEqual(currUserResp);

    //check that experiment loaded
    expect(scopeEdit.newExp).toEqual(expResp);

  });

  it('should load in create mode if route params = new', function () {
    expect(scope.editMode).toBe(false);
    expect(scope.pageTitle).toBe('Create New Experiment');

    httpBackend.flush();

    //check protocol list & users
    expect(scope.protocolValues.length).toBe(protocolResp.totalCount);
    expect(scope.assignedUsers[0]).toEqual(currUserResp);

    //check that experiment is fresh with no data
    expect(scope.newExp).toEqual({});
  });

  it('try to assign new user to experiment, no selection', function(){
    httpBackend.flush();

    var originalAvailCount=scope.availUsers.length;
    var orginalAssignedCount=scope.assignedUsers.length;
    scope.assignUser();
    expect(scope.availUsers.length).toBe(originalAvailCount);
    expect(scope.assignedUsers.length).toBe(orginalAssignedCount);
  });

  it('try to assign new user to experiment, single selection', function(){
    httpBackend.flush();

    var originalAvailCount=scope.availUsers.length;
    var originalAssignedCount=scope.assignedUsers.length;
    var movedObjId=scope.availUsers[0].id;

    scope.availUsers[0].isSelected=true;
    scope.assignUser();

    expect(scope.availUsers.length).toBe(originalAvailCount-1);
    expect(scope.assignedUsers.length).toBe(originalAssignedCount+1);
    expect(scope.assignedUsers[scope.assignedUsers.length-1].id).toBe(movedObjId);
  });

  it('try to remove a user from experiment, no selection', function(){
    httpBackend.flush();

    var originalAvailCount=scope.availUsers.length;
    var originalAssignedCount=scope.assignedUsers.length;
    scope.removeUser();

    // no changes because no selection
    expect(scope.availUsers.length).toBe(originalAvailCount);
    expect(scope.assignedUsers.length).toBe(originalAssignedCount);
  });

  it('try to remove current user from experiment', function(){
    httpBackend.flush();

    var originalAvailCount=scope.availUsers.length;
    var originalAssignedCount=scope.assignedUsers.length;

    scope.assignedUsers[0].isSelected=true;
    scope.removeUser();

    expect(scope.availUsers.length).toBe(originalAvailCount);
    expect(scope.assignedUsers.length).toBe(originalAssignedCount);
    expect(scope.errorText).toContain('You cannot remove yourself');
  });

  it('try to remove assigned user from experiment', function(){
    httpBackend.flush();

    var testUser= {id:100,isSelected:true};
    scope.assignedUsers.push(testUser);
    var originalAvailCount=scope.availUsers.length;
    var originalAssignedCount=scope.assignedUsers.length;

    scope.removeUser();

    expect(scope.availUsers.length).toBe(originalAvailCount+1);
    expect(scope.assignedUsers.length).toBe(originalAssignedCount-1);
    expect(scope.availUsers[scope.availUsers.length-1]).toBe(testUser);
  });

  it('try to save experiment', function(){
    httpBackend.flush();

    var testId=100;
    var someExp={
      "name": "experiment omega",
      "description": "Experiment using the Omega protocol",
      "labels": [],
      "status": "UNPUBLISHED",
      "created": "2015-04-06T23:08:44.134-04:00",
      "protocol": protocolResp.values[1]
    };

    scope.newExp=someExp;
    scope.saveExp();
    httpBackend.whenPUT("services/rest/experiment").respond({id:testId});
    httpBackend.whenPOST("services/rest/experiment/"+testId+"/members").respond("ok");
    httpBackend.flush();

    expect(location.saved).toBe('/experiment');


  });

});
