'use strict';

describe('Controller: ImportResultsCtrl', function () {
// load the controller's module
  beforeEach(module('we99App'));

  var ImportResultsCtrl,
    scope, fakeModal, fakeOption, fakePlateTypeModal, fakeUpload;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope,$log) {
    scope = $rootScope.$new();

    fakeUpload = {
      upload: function (config) {
        this.savedConfig = config;
        // return fake promises for progress, success, error
        return {
          progress: function (f) {
            return {
              success: function (f) {
                return {
                  error: function (f) {
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
        this.result.confirmCallBack( item );
      },
      dismiss: function( type ) {
        this.canceled=true;
        //The user clicked cancel on the modal dialog, call the stored cancel callback
        this.result.cancelCallback( type );
      },
      canceled: false,
      success: false
    };

    ImportResultsCtrl = $controller('ImportResultsCtrl', {
      $scope: scope,
      $upload: fakeUpload,
      $log: $log,
      $modalInstance: fakeModal,
      experiment: {}
    });
  }));


  it('should start with a defined experiment and success=false ', function () {
    expect(scope.experiment).toBeDefined();
    expect(scope.experiment).not.toBeNull();
    expect(scope.success).toBe(false);
  });

  it('should call upload service when files are added', function () {
    scope.files=['newfile'];
    scope.upload();

    expect(fakeUpload.savedConfig).toBeDefined();
    expect(fakeUpload.savedConfig).not.toBeNull();

    expect(fakeUpload.savedConfig.method).toBe("POST");
    expect(fakeUpload.savedConfig.file).toBe("newfile");
    expect(fakeUpload.savedConfig.url).toContain("services/rest");
  });

});
