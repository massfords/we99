'use strict';

describe('Controller: CompoundsCtrl', function () {

    // load the controller's module
    beforeEach(module('we99App'));

    var CompoundsCtrl,
        scope,
        httpBackend,compoundResp,filteredCompoundResp;

    compoundResp =  {
      "totalCount": 297,
      "page": 0,
      "pageSize": 10,
      "values": [
        {
          "id": 1,
          "name": "Acetaldehyde"
        },
        {
          "id": 2,
          "name": "Acetamide"
        },
        {
          "id": 3,
          "name": "Acetic acid"
        },
        {
          "id": 4,
          "name": "Acetone"
        },
        {
          "id": 5,
          "name": "Acetonitrile"
        },
        {
          "id": 6,
          "name": "Aluminium chloride"
        },
        {
          "id": 7,
          "name": "Aluminium nitrate"
        },
        {
          "id": 8,
          "name": "Aluminium sulfate"
        },
        {
          "id": 17,
          "name": "Ammonia"
        },
        {
          "id": 9,
          "name": "Ammonium acetate"
        }
      ]
    }

  filteredCompoundResp={
    "totalCount": 5,
    "page": 0,
    "pageSize": 10,
    "values": [
      {
        "id": 9,
        "name": "Ammonium acetate"
      },
      {
        "id": 77,
        "name": "Lead(II) acetate"
      },
      {
        "id": 80,
        "name": "Lead(IV) acetate"
      },
      {
        "id": 95,
        "name": "Methyl acetate"
      },
      {
        "id": 136,
        "name": "Sodium acetate"
      }
    ]
  };


    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope,$httpBackend) {
        scope = $rootScope.$new();

        // Setup unit test Rest Calls
        httpBackend = $httpBackend;
        httpBackend.whenGET("services/rest/compound/?page=0&pageSize=10&q=").respond(compoundResp);




      CompoundsCtrl = $controller('CompoundsCtrl', {
            $scope: scope
        });
    }));


    it('should retrieve compound list on init', function () {
        httpBackend.flush();
        expect(scope.compoundList.length).toBe(compoundResp.values.length);
    });

  it('should repeat get on compound list whenever searchFilter variable is changed', function () {
    httpBackend.flush();
    httpBackend.whenGET("services/rest/compound/?page=0&pageSize=10&q=acetate").respond(filteredCompoundResp);
    scope.searchFilter="acetate";
    httpBackend.flush();
    expect(scope.compoundList.length).toBe(filteredCompoundResp.values.length);
  });



});
