'use strict';

describe('Directive: addplate', function () {

  // load the directive's module
  beforeEach(module('we99App'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<addplate></addplate>');
    element = $compile(element)(scope);
    console.info(element);
    expect(element.text()).toBe('wire test');
  }));

  //it('should instantiate with an experiment', function(){
  //  expect(scope.experiment).toBeDefined();
  //});

});

describe('Controller: AddPlateCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var AddPlateCtrl,
    scope;




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
                  "name": "lbl",
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
                  "name": "lbl",
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
                  "name": "lbl",
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
                  "name": "lbl",
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
                  "name": "lbl",
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

  beforeEach(inject(function($httpBackend) {
    $httpBackend.whenGET('/services/rest/plateType/').response(JSON.stringify(samplePlateTypeResp));
    $httpBackend.whenGET('/services/rest/plateMap/').response(JSON.stringify(samplePlateMapsResp));
    $httpBackend.whenGET('/services/rest/compound?q=ammo').response(JSON.stringify(sampleCompoundTypeAheadResp))
  }));


  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, $httpBackend) {
    scope = $rootScope.$new();
    scope.experimentName = "Project X";
    AddPlateCtrl = $controller('AddPlateCtrl', {
      $scope: scope
    });
    $httpBackend.flush();
  }));

  describe('Plate Type and Plate Map Selection', function() {
    it('should have a list of plate types on load', function () {
      expect(scope.plateTypes).toEqual(samplePlateTypeResp.values);
    });

    it('should initialize with no selectedPlateType', function () {
      expect(scope.selectedPlateType).toBeNull();
    });

    it('should NOT change plateTypes when a plate type is selected', function () {
      var expectedPlateTypes = angular.copy(scope.plateTypes);
      // valid selection
      scope.selectedPlateType = samplePlateTypeResp.values[0];
      scope.$digest();
      expect(scope.plateTypes).toEqual(expectedPlateTypes);
      // invalid selection
      scope.selectedPlateType = samplePlateTypeResp.values[0];
      scope.$digest();
      scope.selectedPlateType = null;
      scope.$digest();
      expect(scope.plateTypes).toEqual(expectedPlateTypes);
    });

    it('should change selectedPlateType when a plate type is selected', function () {
      // valid selection
      scope.selectedPlateType = samplePlateTypeResp.values[0];
      scope.$digest();
      expect(scope.plateTypes).toEqual(samplePlateTypeResp.values[0]);
    });

    it('should initialize with no plateMaps', function () {
      expect(scope.plateMaps).toBeNull();
    });

    it('should get a list of valid plateMaps when a plate type is selected', function () {
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
                  "name": "lbl",
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
                  "name": "lbl",
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
      scope.$digest();
      expect(scope.selectedPlateType.dim.rows).toBe(8);
      expect(scope.selectedPlateType.dim.cols).toBe(12);
      expect(scope.plateMaps).toEqual(expectedPlateMaps);
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
                  "name": "lbl",
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
                  "name": "lbl",
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
                  "name": "lbl",
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
                  "name": "lbl",
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
                  "name": "lbl",
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
      expect(scope.plateMaps).not.toEqual(expectedPlateMaps);

      // Change to desired selection
      scope.selectedPlateType = samplePlateTypeResp.values[1];
      scope.$digest();
      expect(scope.selectedPlateType.dim.rows).toBe(16);
      expect(scope.selectedPlateType.dim.cols).toBe(24);
      expect(scope.plateMaps).toEqual(expectedPlateMaps);
    });

    it('should NOT change plateMaps when a plate map is selected', function () {
      scope.selectedPlateType = samplePlateTypeResp[0];
      scope.$digest();

      var expectedPlateMaps = angular.copy(scope.plateMaps);

      // valid selection
      scope.selectedPlateMap = samplePlateMapsResp[0];
      scope.$digest();
      expect(scope.plateMaps).toBe(expectedPlateMaps);
      // invalid selection
      scope.selectedPlateMap = samplePlateMapsResp[0];
      scope.$digest();
      scope.selectedPlateType = null;
      scope.$digest();
      expect(scope.plateTypes).toBe(expectedPlateMaps);
    });

    describe('Replicates options', function(){
      it('should call replicates function at least once per selected plate map label', function() {
        spyOn(scope, 'computeReplicates');
        var labelSet = {};

        scope.selectedPlateType = samplePlateTypeResp.values[0];
        scope.selectedPlateMap = samplePlateMapsResp.values[0];
        scope.$digest();

        // get labels
        scope.selectedPlateMap.wells.forEach(function(well) {

          well.labels.forEach(function(label){
            if (!labelSet.hasOwnProperty(label.name)) {
              labelSet[label.name] = true;
            }
          });
        });

        expect(scope.computeReplicates.calls.length).toEqual(Object.keys(labelSet).length);
      });

      it('should provide a options that are a factor of the count', function() {
        expect(scope.computeReplicates(0)).toEqual([]);
        expect(scope.computeReplicates(-1)).toThrow();
        expect(scope.computeReplicates(1)).toEqual([1]);
        expect(scope.computeReplicates(17)).toEqual([1,17]);
        expect(scope.computeReplicates(10)).toEqual([1,2,5,10]);
        expect(scope.computeReplicates(12)).toEqual([1,2,3,4,6,12]);
      });
    });

    describe('compound typeahead', function(){
      it('should call typeahead function when typing into a compound box', function(){
        // TODO
      });
      it('should get typeahead values', function(){
        expect(scope.retrieveCompoundMatches('ammo')).toEqual(sampleCompoundTypeAheadResp.values);
      });
    });

  });
});
