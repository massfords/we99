'use strict';

describe('Directive: addplate', function () {

  // load the directive's module
  beforeEach(module('we99App'));

  var element,
    scope;

  var samplePlateTypeResp = [
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
  ];

  beforeEach(inject(function($httpBackend) {
    $httpBackend.whenGET('/services/rest/plateType/').response(samplePlateTypeResp);
  }));

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<addplate></addplate>');
    element = $compile(element)(scope);
    console.info(element);
    expect(element.text()).toBe('wire test');
  }));
});

describe('Controller: AddPlateCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var AddPlateCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    scope.experiment = "Project X";
    AddPlateCtrl = $controller('AddPlateCtrl', {
      $scope: scope
    });
  }));

  it('should instantiate with an experiment', function(){
    expect(scope.experiment).toBeDefined();
  });

  it('should ')
});
