'use strict';

describe('Directive: addplate', function () {

  // load the directive's module
  beforeEach(module('we99App'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));


  // FIXME: TIM PLEASE TAKE A LOOK AT THIS AND SEE IF YOU CAN GET THIS WORKING
  //it('should make hidden element visible', inject(function ($compile) {
  //  element = angular.element('<addplate></addplate>');
  //  element = $compile(element)(scope);
  //  console.info(element);
  //  scope.$digest();
  //  expect(element.text()).toBe('wire test');
  //}));

  //it('should instantiate with an experiment', function(){
  //  expect(scope.experiment).toBeDefined();
  //});

});

describe('Controller: AddPlateCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var AddPlateCtrl,
    scope,
    $httpBackend;




  var samplePlateTypeResp = {values: [
    {
      "id": 1,
      "name": "Corning-3788",
      "description": "Clear high-grade polystyrene for research diagnostic assays Formulated for uniform binding\\",
      "dim": {
        "rows": 8,
        "cols": 12
      },
      "manufacturer": "Corning",
      "orderLink": "http://www.coleparmer.com/Product/Corning_Plate_96_Well_ps_round_100_cs_Clear/UX-01728-07"
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
      "orderLink": "http://www.universalmedicalinc.com/BrandTech-384-Well-Plate-pureGrade-Non-Treated-p/781620-UMI.htm"
    }
    ]},
      samplePlateMapsResp = {values:[
        {
          "id": 300,
          "name": "pme",
          "description": "foo123",
          "wells": [
            {
              "id": 800,
              "coordinate": {
                "row": 4,
                "col": 5
              },
              "labels": [
                {
                  "name": "lbl1",
                  "value": "ABC"
                }
              ],
              "type": "COMP"
            }
          ],
          "dim": {
            "rows": 5,
            "cols": 6
          }
        },
        {
          "id": 301,
          "name": "pme",
          "description": "foo123",
          "wells": [
            {
              "id": 800,
              "coordinate": {
                "row": 4,
                "col": 5
              },
              "labels": [
                {
                  "name": "lbl1",
                  "value": "ABC"
                }
              ],
              "type": "COMP"
            }
          ],
          "dim": {
            "rows": 8,
            "cols": 12
          }
        },
        {
          "id": 302,
          "name": "pme",
          "description": "foo123",
          "wells": [
            {
              "id": 800,
              "coordinate": {
                "row": 4,
                "col": 5
              },
              "labels": [
                {
                  "name": "lbl1",
                  "value": "ABC"
                }
              ],
              "type": "COMP"
            }
          ],
          "dim": {
            "rows": 5,
            "cols": 20
          }
        },
        {
          "id": 302,
          "name": "pme",
          "description": "foo123",
          "wells": [
            {
              "id": 800,
              "coordinate": {
                "row": 4,
                "col": 5
              },
              "labels": [
                {
                  "name": "lbl1",
                  "value": "ABC"
                }
              ],
              "type": "COMP"
            }
          ],
          "dim": {
            "rows": 9,
            "cols": 13
          }
        },
        {
          "id": 302,
          "name": "pme",
          "description": "foo123",
          "wells": [
            {
              "id": 800,
              "coordinate": {
                "row": 4,
                "col": 5
              },
              "labels": [
                {
                  "name": "lbl1",
                  "value": "ABC"
                }
              ],
              "type": "COMP"
            }
          ],
          "dim": {
            "rows": 16,
            "cols": 25
          }
        }
      ]},
      sampleCompoundTypeAheadResp = {
        "totalCount": 10,
        "page": 100,
        "values": [
          {
            "id": 17,
            "name": "Ammonia"
          },
          {
            "id": 9,
            "name": "Ammonium acetate"
          },
          {
            "id": 10,
            "name": "Ammonium carbonate"
          },
          {
            "id": 11,
            "name": "Ammonium chloride"
          },
          {
            "id": 12,
            "name": "Ammonium dichromate"
          },
          {
            "id": 13,
            "name": "Ammonium hydroxide"
          },
          {
            "id": 14,
            "name": "Ammonium nitrate"
          },
          {
            "id": 15,
            "name": "Ammonium oxalate"
          },
          {
            "id": 16,
            "name": "Ammonium sulfate"
          },
          {
            "id": 69,
            "name": "Iron(II) ammonium sulfate"
          }
        ]
      };

  beforeEach(inject(function(_$httpBackend_) {
    $httpBackend = _$httpBackend_;
    $httpBackend.whenGET('services/rest/plateType').respond(JSON.stringify(samplePlateTypeResp));
    $httpBackend.whenGET('services/rest/plateMap?maxCols=12&maxRows=8').respond(JSON.stringify(samplePlateMapsResp));
    $httpBackend.whenGET('services/rest/plateMap?maxCols=24&maxRows=16').respond(JSON.stringify(samplePlateMapsResp));
    $httpBackend.whenGET('services/rest/compound?q=ammo').respond(JSON.stringify(sampleCompoundTypeAheadResp))
  }));


  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    scope.experimentName = "Project X";
    AddPlateCtrl = $controller('AddPlateCtrl', {
      $scope: scope
    });
  }));

  describe('Plate Type and Plate Map Selection', function() {
    it('should have a list of plate types on load', function () {
      $httpBackend.flush();
      expect(angular.equals(samplePlateTypeResp.values, scope.plateTypes)).toBe(true);
    });

    it('should initialize with no selectedPlateType', function () {
      expect(scope.selectedPlateType).toBeNull();
    });

    it('should NOT change plateTypes when a plate type is selected', function () {
      $httpBackend.flush();
      var expectedPlateTypes = angular.copy(scope.plateTypes);
      // valid selection
      scope.selectedPlateType = samplePlateTypeResp.values[0];
      scope.$digest();
      expect(angular.equals(scope.plateTypes, expectedPlateTypes)).toBe(true);
      // invalid selection
      scope.selectedPlateType = samplePlateTypeResp.values[0];
      scope.$digest();
      scope.selectedPlateType = null;
      scope.$digest();
      expect(angular.equals(scope.plateTypes, expectedPlateTypes)).toBe(true);
    });

    it('should change selectedPlateType when a plate type is selected', function () {
      // valid selection
      scope.selectedPlateType = samplePlateTypeResp.values[0];
      scope.$digest();
      expect(scope.selectedPlateType).toEqual(samplePlateTypeResp.values[0]);
    });

    it('should initialize with no plateMaps', function () {
      expect(scope.plateMaps).toBeNull();
    });

    it('should get a list of valid plateMaps when plateMapsForPlateType is triggered', function () {
      var expectedPlateMaps = [
        {
          "id": 300,
          "name": "pme",
          "description": "foo123",
          "wells": [
            {
              "id": 800,
              "coordinate": {
                "row": 4,
                "col": 5
              },
              "labels": [
                {
                  "name": "lbl1",
                  "value": "ABC"
                }
              ],
              "type": "COMP"
            }
          ],
          "dim": {
            "rows": 5,
            "cols": 6
          }
        },
        {
          "id": 301,
          "name": "pme",
          "description": "foo123",
          "wells": [
            {
              "id": 800,
              "coordinate": {
                "row": 4,
                "col": 5
              },
              "labels": [
                {
                  "name": "lbl1",
                  "value": "ABC"
                }
              ],
              "type": "COMP"
            }
          ],
          "dim": {
            "rows": 8,
            "cols": 12
          }
        }
      ];

      scope.selectedPlateType = samplePlateTypeResp.values[0];
      expect(scope.selectedPlateType.dim.rows).toBe(8);
      expect(scope.selectedPlateType.dim.cols).toBe(12);
      $httpBackend.expectGET('services/rest/plateMap?maxCols=12&maxRows=8');
      scope.plateMapsForPlateType();
      $httpBackend.flush();
    });

    it('should change the list of valid plateMaps when a different plate type is selected', function () {
      var expectedPlateMaps = [
        {
          "id": 300,
          "name": "pme",
          "description": "foo123",
          "wells": [
            {
              "id": 800,
              "coordinate": {
                "row": 4,
                "col": 5
              },
              "labels": [
                {
                  "name": "lbl1",
                  "value": "ABC"
                }
              ],
              "type": "COMP"
            }
          ],
          "dim": {
            "rows": 5,
            "cols": 6
          }
        },
        {
          "id": 301,
          "name": "pme",
          "description": "foo123",
          "wells": [
            {
              "id": 800,
              "coordinate": {
                "row": 4,
                "col": 5
              },
              "labels": [
                {
                  "name": "lbl1",
                  "value": "ABC"
                }
              ],
              "type": "COMP"
            }
          ],
          "dim": {
            "rows": 8,
            "cols": 12
          }
        },
        {
          "id": 302,
          "name": "pme",
          "description": "foo123",
          "wells": [
            {
              "id": 800,
              "coordinate": {
                "row": 4,
                "col": 5
              },
              "labels": [
                {
                  "name": "lbl1",
                  "value": "ABC"
                }
              ],
              "type": "COMP"
            }
          ],
          "dim": {
            "rows": 5,
            "cols": 20
          }
        },
        {
          "id": 302,
          "name": "pme",
          "description": "foo123",
          "wells": [
            {
              "id": 800,
              "coordinate": {
                "row": 4,
                "col": 5
              },
              "labels": [
                {
                  "name": "lbl1",
                  "value": "ABC"
                }
              ],
              "type": "COMP"
            }
          ],
          "dim": {
            "rows": 9,
            "cols": 13
          }
        },
        {
          "id": 302,
          "name": "pme",
          "description": "foo123",
          "wells": [
            {
              "id": 800,
              "coordinate": {
                "row": 4,
                "col": 5
              },
              "labels": [
                {
                  "name": "lbl1",
                  "value": "ABC"
                }
              ],
              "type": "COMP"
            }
          ],
          "dim": {
            "rows": 16,
            "cols": 25
          }
        }
      ];

      // Select a different selection
      scope.selectedPlateType = samplePlateTypeResp.values[0];
      scope.$digest();
      expect(scope.selectedPlateType.dim.rows).toBe(8);
      expect(scope.selectedPlateType.dim.cols).toBe(12);
      $httpBackend.expectGET('services/rest/plateMap?maxCols=12&maxRows=8');
      scope.plateMapsForPlateType();
      $httpBackend.flush();

      // Change to desired selection
      scope.selectedPlateType = samplePlateTypeResp.values[1];
      scope.$digest();
      expect(scope.selectedPlateType.dim.rows).toBe(16);
      expect(scope.selectedPlateType.dim.cols).toBe(24);
      $httpBackend.expectGET('services/rest/plateMap?maxCols=24&maxRows=16');
      scope.plateMapsForPlateType();
      $httpBackend.flush();
    });

    it('should NOT change plateTypes or selectedPlateTypes when selected', function(){
      scope.plateTypes = samplePlateTypeResp.values;
      scope.selectedPlateType = samplePlateTypeResp.values[0];
      $httpBackend.flush();
      scope.$digest();
      scope.plateMapsForPlateType();
      $httpBackend.flush();
      expect(angular.equals(scope.plateTypes, samplePlateTypeResp.values)).toBe(true);
      expect(angular.equals(scope.selectedPlateType, samplePlateTypeResp.values[0])).toBe(true);
    });

    it('should NOT change plateMaps when a plate map is selected', function () {
      scope.selectedPlateType = samplePlateTypeResp.values[0];
      scope.$digest();

      var expectedPlateMaps = angular.copy(scope.plateMaps);

      // valid selection
      scope.selectedPlateMap = samplePlateMapsResp[0];
      scope.$digest();
      expect(angular.equals(scope.plateMaps,expectedPlateMaps)).toBe(true);
      // invalid selection
      scope.selectedPlateMap = samplePlateMapsResp[0];
      scope.$digest();
      scope.selectedPlateType = null;
      scope.$digest();
      expect(angular.equals(scope.plateMaps,expectedPlateMaps)).toBe(true);
    });

    describe('Replicates options', function(){

      it('should not accept counts < 0', function() {
        expect(function(){scope.computeReplicates(-1);}).toThrowError(Error);
      });

      it('should provide a options that are a factor of the count', function() {
        expect(scope.computeReplicates(0)).toEqual([]);
        expect(scope.computeReplicates(1)).toEqual([1]);
        expect(scope.computeReplicates(17)).toEqual([1,17]);
        expect(scope.computeReplicates(10)).toEqual([1,2,5,10]);
        expect(scope.computeReplicates(12)).toEqual([1,2,3,4,6,12]);
      });
    });

    describe('compound typeahead', function(){
      it('should get typeahead values', function(){
        expect(scope.retrieveCompoundMatches('ammo')).toEqual(sampleCompoundTypeAheadResp.values);
      });
    });

  });
});

describe('Service: LabelTableSvc', function () {

  // load the service's module
  beforeEach(module('we99App'));

  // instantiate service
  var LabelTableSvc;
  var templatePlateMap = {
    "id": 300,
    "name": "pme",
    "description": "foo123",
    "wells": [
      {
        "id": 800,
        "coordinate": {
          "row": 4,
          "col": 5
        },
        "labels": [
          {
            "name": "lbl1",
            "value": "ABC"
          }
        ],
        "type": "COMP"
      }
    ],
    "dim": {
      "rows": 7,
      "cols": 8
    }
  };

  beforeEach(inject(function (_LabelTableSvc_) {
    LabelTableSvc = _LabelTableSvc_;
  }));

  describe('plateMapToLabelTable', function() {
    it('should return an empty array if platemap is null', function() {
      expect(LabelTableSvc.plateMapToLabelTable(null)).toEqual([]);
    });
    it('should error if plate map object does not have wells', function() {
      var plateMap = angular.copy(templatePlateMap);
      delete plateMap.wells;
      expect(function(){LabelTableSvc.plateMapToLabelTable(plateMap);}).toThrowError(TypeError)
    });
    it('should return an empty array if plate map object does not have labels', function() {
      var plateMap = angular.copy(templatePlateMap);
      delete plateMap.wells[0].labels;
      expect(LabelTableSvc.plateMapToLabelTable(plateMap)).toEqual([]);
    });
    it('should error if plate map object does not have a type', function() {
      var plateMap = angular.copy(templatePlateMap);
      delete plateMap.wells.forEach(function(well){delete well.type;});
      expect(function(){LabelTableSvc.plateMapToLabelTable(plateMap);}).toThrowError(TypeError);
    });
    it('should create a proper label table given a well map with wells, some of which contain the same label', function(){
      var plateMap = angular.copy(templatePlateMap);
      // Populate wells with ABC (COMP : 2x), DEF (EXP : 2x), GHI (EXP : 1x)
      plateMap.wells = [
        {
          "id": 800,
          "coordinate": {
            "row": 0,
            "col": 0
          },
          "labels": [
            {
              "name": "lbl1",
              "value": "ABC"
            }
          ],
          "type": "COMP"
        },
        {
          "id": 801,
          "coordinate": {
            "row": 1,
            "col": 2
          },
          "labels": [
            {
              "name": "lbl1",
              "value": "DEF"
            }
          ],
          "type": "EXP"
        },
        {
          "id": 802,
          "coordinate": {
            "row": 3,
            "col": 4
          },
          "labels": [
            {
              "name": "lbl1",
              "value": "ABC"
            },
            {
              "name": "non-lbl",
              "value": "DEF"
            }
          ],
          "type": "COMP"
        },
        {
          "id": 803,
          "coordinate": {
            "row": 5,
            "col": 6
          },
          "labels": [
            {
              "name": "lbl1",
              "value": "DEF"
            },
            {
              "name": "non-lbl",
              "value": "ABC"
            },
            {
              "name": "non-lbl2",
              "value": "DEF"
            }

          ],
          "type": "EXP"
        },
        {
          "id": 800,
          "coordinate": {
            "row": 4,
            "col": 4
          },
          "labels": [
            {
              "name": "lbl1",
              "value": "GHI"
            }
          ],
          "type": "EXP"
        }
      ];

      var LabelTableRow = LabelTableSvc.LabelTableRow,
          labelTable = LabelTableSvc.plateMapToLabelTable(plateMap);
      // sort for predictable return value to compare to
      labelTable = labelTable.sort(function (l, r) {
        if (l.label > r.label) {
          return 1;
        }
        if (l.label < r.label) {
          return -1;
        }
        // a must be equal to b
        return 0;
      });

      // expected rows
      var expectedLabelTable = [new LabelTableRow('ABC', 'COMP'), new LabelTableRow('DEF', 'EXP'), new LabelTableRow('GHI', 'EXP')];
      expectedLabelTable[0].count = 2;
      expectedLabelTable[1].count = 2;
      expectedLabelTable[2].count = 1;

      expect(angular.equals(labelTable, expectedLabelTable)).toBe(true);
    });

  });
});
