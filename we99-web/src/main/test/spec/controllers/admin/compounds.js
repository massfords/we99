'use strict';

describe('Controller: CompoundsCtrl', function () {

    // load the controller's module
    beforeEach(module('we99App'));

    var CompoundsCtrl,ImportCompoundsCtrl,
        scope, importScope,
        httpBackend,compoundResp,filteredCompoundResp,
        fakeUpload, fakeModal;

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
      then: function(confirmCallback) {
        //Store the callbacks for later when the user clicks on the OK or Cancel button of the dialog
        this.confirmCallBack = confirmCallback;
        this.cancelCallback = confirmCallback;
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
        importScope = $rootScope.$new();

        // Setup unit test Rest Calls
        httpBackend = $httpBackend;
        httpBackend.whenGET("services/rest/compound/?page=0&pageSize=10&q=").respond(compoundResp);




      CompoundsCtrl = $controller('CompoundsCtrl', {
            $scope: scope,
            $modal: {open: function(){return fakeModal;}}
        });

      ImportCompoundsCtrl = $controller('ImportCompoundsCtrl', {
        $scope: importScope,
        $modalInstance: fakeModal,
        $upload: fakeUpload,
        $log: {log: function(){}}
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

  it('should call upload service when files are added', function () {
    importScope.files=['newfile'];
    importScope.upload(importScope.files);

    expect(fakeUpload.savedConfig).toBeDefined();
    expect(fakeUpload.savedConfig).not.toBeNull();

    expect(fakeUpload.savedConfig.method).toBe("POST");
    expect(fakeUpload.savedConfig.file).toBe("newfile");
    expect(fakeUpload.savedConfig.url).toContain("services/rest");

    importScope.dismiss();
    expect(fakeModal.canceled).toBe(true);

  });

  it('should start tour', function () {
    httpBackend.flush();
    scope.startTour();

    expect(scope.startJoyRide).toBe(true);
  });

  it('should open modal for import', function () {
    httpBackend.flush();
    scope.importCompounds();
    fakeModal.close('good');


    httpBackend.whenGET("services/rest/compound/?page=0&pageSize=10&q=").respond(compoundResp);
    httpBackend.flush();

  });






});
