'use strict';

describe('Controller: PlateMapEditorCtrl', function () {

  // load the controller's module
  beforeEach(module('we99App'));

  var plateMapEditorCtrl,AddPlateMapCtrl,
    httpBackend,
    scope, modalScope,
    fakeUpload,fakeModal;

  fakeUpload = {
    upload: function (config) {
      this.savedConfig = config;
      // return fake promises for progress, success, error
      return {
        progress: function (f) {
          var evt={loaded: 1, total:1,config:{file:{name:'myfile'}}};
          f(evt);
          return {
            success: function (f) {
              f('good');
              return {
                error: function (f) {
                  f('error case');
                }
              }
            }
          }
        }
      };
    }
  };
  fakeModal = {
    result: {
      then: function(confirmCallback, cancelCallback) {
        //Store the callbacks for later when the user clicks on the OK or Cancel button of the dialog
        this.confirmCallBack = confirmCallback;
        this.cancelCallback = cancelCallback;
      }
    },
    close: function( item ) {

      this.success=true;
      //The user clicked OK on the modal dialog, call the stored confirm callback with the selected item
      if(this.result.confirmCallBack)
        this.result.confirmCallBack( item );
    },
    dismiss: function( type ) {
      this.canceled=true;
      //The user clicked cancel on the modal dialog, call the stored cancel callback
      if(this.result.cancelCallback)
        this.result.cancelCallback( type );
    },
    canceled: false,
    success: false
  };

  var queryPlateTypesResponse = {
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


  // Set up Mock HTTP Responses
  beforeEach(inject(function($httpBackend) {
    httpBackend = $httpBackend;
    httpBackend.whenGET("services/rest/plateMap").respond(JSON.stringify(queryPlateTypesResponse));

    httpBackend.whenDELETE("services/rest/plateMap/16").respond("Deleted plate map.");
  }));

  //it('should call refreshPlateMapsList on load'), function($controller, $rootScope) {
  //  scope = $rootScope.$new();
  //
  //  PlateMapEditorCtrl = $controller('PlateMapEditorCtrl', {
  //    $scope: scope
  //  });
  //});

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope =
    modalScope = $rootScope.$new();
    plateMapEditorCtrl = $controller('PlateMapEditorCtrl', {
      $scope: scope,
      $modal: {open:function(config){return fakeModal;}}
    });

    AddPlateMapCtrl = $controller('AddPlateMapCtrl', {
      $scope: modalScope,
      $upload: fakeUpload,
      $modalInstance: fakeModal
    });

    httpBackend.flush();
  }));

  it('should show a list of existing platemaps on load', function () {
    expect(scope.plateMaps.length).toBe(queryPlateTypesResponse.values.length);
  });

  it('should upload plate map csv', function () {
    modalScope.files=['fakefile'];
    modalScope.upload(modalScope.files);

    expect(fakeModal.canceled).toBe(false);
    expect(fakeModal.success).toBe(true);
  });

  it('should cancel modal', function () {
    modalScope.cancel();
    expect(fakeModal.canceled).toBe(true);
  });

  it('should call upload function after add plate call', function () {
    modalScope.name = 'name';
    modalScope.description = 'description';
    modalScope.plateMapFile = 'file';
    modalScope.addPlateMap();

    expect(fakeUpload.savedConfig).toBeDefined();
    expect(fakeModal.success).toBe(true);
  });

  it('should show add plate map modal', function () {
    scope.showAddPlateMap();
    fakeModal.result.confirmCallBack('woot');
    fakeModal.result.cancelCallback();
    expect(scope.modalSuccessMsg).toBe('woot');
  });

  it('should remove plate map', function () {
    var size=scope.plateMaps.length;
    scope.removePlateMap({id:16});
    httpBackend.flush();
    expect(scope.plateMaps.length).toBe(size-1);

  });



});
