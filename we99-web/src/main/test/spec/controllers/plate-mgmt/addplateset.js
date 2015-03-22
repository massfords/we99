'use strict';

describe('Controller: AddPlatesetCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var AddPlatesetCtrl,
    scope,
    httpBackend;

  var queryPlateTypesResponse = {
    "values": [
      {
        "id": 1,
        "name": "Corning-3788",
        "description": "Clear high-grade polystyrene for research diagnostic assays Formulated for uniform binding\\",
        "dim": {
          "rows": 8,
          "cols": 12
        },
        "manufacturer": "Corning",
        "link": "http://www.coleparmer.com/Product/Corning_Plate_96_Well_ps_round_100_cs_Clear/UX-01728-07"
      },
      {
        "id": 2,
        "name": "BrandTech 384",
        "description": "pureGrade BRANDplates feature non-treated\\",
        "dim": {
          "rows": 16,
          "cols": 24
        },
        "manufacturer": "BrandTech",
        "link": "http://www.universalmedicalinc.com/BrandTech-384-Well-Plate-pureGrade-Non-Treated-p/781620-UMI.htm"
      },
      {
        "id": 3,
        "name": "plateTypebaaccb7e-f22a-42d0-87c4-58e2509f988e",
        "dim": {
          "rows": 4,
          "cols": 3
        },
        "manufacturer": "Foo Inc."
      },
      {
        "id": 4,
        "name": "pt0a946158-98c4-4033-b6a1-8d46604e6b94",
        "dim": {
          "rows": 10,
          "cols": 10
        },
        "manufacturer": "Foo Inc."
      },
      {
        "id": 5,
        "name": "ptac597c0c-5257-4221-a431-6c3b472b6735",
        "dim": {
          "rows": 20,
          "cols": 20
        },
        "manufacturer": "Foo Inc."
      },
      {
        "id": 6,
        "name": "pt0bcecf0d-2877-4de9-b3a3-76840fed966e",
        "dim": {
          "rows": 30,
          "cols": 30
        },
        "manufacturer": "Foo Inc."
      },
      {
        "id": 7,
        "name": "ptac833349-56af-48ac-8439-8f02e1b6b75d",
        "dim": {
          "rows": 40,
          "cols": 40
        },
        "manufacturer": "Foo Inc."
      },
      {
        "id": 8,
        "name": "pt8c49bf9a-5985-4c83-a2c8-7af3bb68fe7f",
        "dim": {
          "rows": 50,
          "cols": 50
        },
        "manufacturer": "Foo Inc."
      },
      {
        "id": 9,
        "name": "pt328211ed-f3e6-4e2f-a716-3b8d496cce4a",
        "dim": {
          "rows": 60,
          "cols": 60
        },
        "manufacturer": "Foo Inc."
      },
      {
        "id": 10,
        "name": "pt45c621b7-ac4f-4da8-be13-9ae2f91bbb2c",
        "dim": {
          "rows": 70,
          "cols": 70
        },
        "manufacturer": "Foo Inc."
      },
      {
        "id": 11,
        "name": "pt2e621210-144f-4553-9719-a5dc004a1ede",
        "dim": {
          "rows": 80,
          "cols": 80
        },
        "manufacturer": "Foo Inc."
      },
      {
        "id": 12,
        "name": "ptb2fc89fe-6402-474e-b8a5-d5a58fe26902",
        "dim": {
          "rows": 90,
          "cols": 90
        },
        "manufacturer": "Foo Inc."
      },
      {
        "id": 13,
        "name": "ptd6ff68be-49d8-412c-911a-6e5e0005eb1c",
        "dim": {
          "rows": 10,
          "cols": 10
        },
        "manufacturer": "Foo Inc."
      },
      {
        "id": 14,
        "name": "pt04c61f27-7f7c-4c8e-9ee2-f00cbb7cec7f",
        "dim": {
          "rows": 10,
          "cols": 10
        },
        "manufacturer": "Foo Inc."
      }
    ]
  };

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, $httpBackend) {

    // Setup unit test Rest Calls
    httpBackend = $httpBackend;
    httpBackend.expect("services/rest/plateType").respond(queryPlateTypesResponse);



    // Set up everything else
    scope = $rootScope.$new();
    AddPlatesetCtrl = $controller('AddPlatesetCtrl', {
      $scope: scope
    });
  }));

  //it('should grab the list of plate types', function () {
  //  expect($scope.plateTypes) == queryPlateTypesResponse;
  //});
});
