'use strict';

describe('Controller: PlateMapEditorCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var PlateMapEditorCtrl,
    httpBackend,
    scope;

  var queryPlateMapResponse = {
    "totalCount": 2,
    "page": 0,
    "values": [
      {
        "id": 16,
        "name": "plateMap-e4dcfc64-4212-4616-9653-576d782b3c7b",
        "description": "my test plate",
        "plateType": {
          "id": 3,
          "name": "plateTypeddc2e61a-9d9b-46a2-b1e8-cfa09e89b2b4",
          "dim": {
            "rows": 4,
            "cols": 3
          },
          "manufacturer": "Foo Inc."
        },
        "wells": []
      },
      {
        "id": 17,
        "name": "plateMap-c906759c-4f36-462c-a857-66337e5832f6",
        "description": "my modified description",
        "plateType": {
          "id": 3,
          "name": "plateTypeddc2e61a-9d9b-46a2-b1e8-cfa09e89b2b4",
          "dim": {
            "rows": 4,
            "cols": 3
          },
          "manufacturer": "Foo Inc."
        },
        "wells": [
          {
            "id": 1442,
            "coordinate": {
              "row": 0,
              "col": 0
            },
            "labels": [
              {
                "id": 1443,
                "name": "loc",
                "value": "well 0,0"
              }
            ],
            "type": "COMP"
          }
        ]
      }
    ]
  };

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    PlateMapEditorCtrl = $controller('PlateMapEditorCtrl', {
      $scope: scope
    });
  }));

// Set up Mock HTTP Responses
  beforeEach(inject(function($httpBackend) {
    httpBackend = $httpBackend;
    httpBackend.expectGET("services/rest/plateMap").respond(JSON.stringify(queryPlateTypesResponse));
  }));

  it('should show a list of existing platemaps on load', function () {
    expect(scope.plateMaps.length).toBe(queryPlateMapResponse.values.length);
  });


});
