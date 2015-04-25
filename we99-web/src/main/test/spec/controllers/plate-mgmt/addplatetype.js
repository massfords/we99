'use strict';

describe('Controller: AddPlateTypeCtrl', function () {
// load the controller's module
  beforeEach(module('we99App'));

  var AddPlateTypeCtrl,
    scope, fakeModal, fakeOption, fakePlateTypeModal;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    fakeOption={isCustom:false,name: 'name',rows:5, cols:5};
    fakePlateTypeModal={create: function(resp,callback){callback();}};
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
        //this.result.confirmCallBack( item );
      },
      dismiss: function( type ) {
        this.canceled=true;
        //The user clicked cancel on the modal dialog, call the stored cancel callback
        //this.result.cancelCallback( type );
      },
      canceled: false,
      success: false
    };
    AddPlateTypeCtrl = $controller('AddPlateTypeCtrl', {
      $scope: scope,
      $modalInstance: fakeModal,
      option: fakeOption,
      PlateTypeModel: fakePlateTypeModal
    });
  }));

  it('should start with an empty plate type', function () {
    expect(scope.plateType).toBeDefined();
    expect(scope.plateType).not.toBeNull();
    expect(scope.plateType.name).toEqual('name Plate');
    expect(scope.plateType.description).toEqual('');
    expect(scope.plateType.manufacturer).toEqual('');
    expect(scope.plateType.material).toEqual('');
    expect(scope.plateType.orderingLink).toEqual('');
    expect(scope.plateType.orderingNotes).toEqual('');
  });

  it('cancel should dismiss modal in canceled state', function () {
    scope.cancel();
    expect(fakeModal.canceled).toBe(true);
  });

  it('should add plate type', function () {
    scope.addPlateType();
    //expect(fakeModal.canceled).toBe(true);
  });


});
