'use strict';

describe('Controller: AdminSettingsCtrl', function () {

    // load the controller's module
    beforeEach(module('we99App'));

    var AdminSettingsCtrl,
        scope,
        httpBackend,filterResp,configResp;

    filterResp ={
        expression: "regexPattern"
    };

    configResp = {
        "host": "smtp.gmail.com",
        "port": 587,
        "username": "we99.2015@gmail.com",
        "password": null,
        "from": "we99.2015@gmail.com"
    };

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope,$httpBackend) {
        scope = $rootScope.$new();

        // Setup unit test Rest Calls
        httpBackend = $httpBackend;
        httpBackend.whenGET("services/rest/settings/email/filter").respond(filterResp);
        httpBackend.whenGET("services/rest/settings/email/config").respond(configResp);

        httpBackend.whenPOST("services/rest/settings/email/filter").respond(filterResp);
        httpBackend.whenPOST("services/rest/settings/email/config").respond(configResp);


        AdminSettingsCtrl = $controller('AdminSettingsCtrl', {
            $scope: scope
        });
    }));


    it('should retrieve email filter settings on init', function () {
        httpBackend.flush();
        expect(scope.emailFilter.expression).toBe('regexPattern');
    });

    it('should retrieve email server settings on init', function () {
        httpBackend.flush();
        expect(scope.configSettings.host).toBe("smtp.gmail.com");
        expect(scope.configSettings.port).toBe(587);
        expect(scope.configSettings.username).toBe("we99.2015@gmail.com");
        expect(scope.configSettings.password).toBeNull();
        expect(scope.configSettings.from).toBe('we99.2015@gmail.com');
    });

  it('should save email server settings on post', function () {
    scope.saveServerSettings()
    httpBackend.flush();
    expect(scope.infoText).toBe("Saved new server email settings.");
  });

  it('should set flag at start tour', function () {
    scope.startTour();
    expect(scope.startJoyRide).toBeTruthy();
  });

  it('should set error message', function () {
    var text='ack an error!';
    AdminSettingsCtrl.setError(text);
    expect(scope.errorText).toBe(text);
    expect(scope.infoText).toBeNull();
  });


});
