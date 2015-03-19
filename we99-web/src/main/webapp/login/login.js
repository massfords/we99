angular.module('we99Login', [

])
  .constant('kRestServer', '/we99/services/rest/')
  .constant('kSignupErrorCode', {
    401: "Access Forbidden.",
    409: "Email address already in use.",
    500: "Error registering. Have the admin check email connection."
  })


  .controller('SignUpController', function ($scope, $http, $timeout, kRestServer, kSignupErrorCode) {

    $scope.signUp = function() {
      var promise = $http.post(kRestServer + 'createAccount', $scope.user);
      promise.then(function() {
        $scope.successTextAlert = "Check your email for the activation link.";
        $scope.showSuccessAlert = true;
      }, function(resp) {
        if (kSignupErrorCode.hasOwnProperty(resp.status)) {
          $scope.dangerTextAlert = kSignupErrorCode[resp.status];
          $scope.showDangerAlert = true;
        } else {
          alert("Unexpected error:"+ resp);
        }
      });
    };

    $scope.loadUser = function() {
      var url = new URI();
      var queryParams = url.search(true);
      $scope.user = {};
      $scope.user.email = queryParams['email'];
      $scope.uuid = queryParams['uuid'];

      var promise = $http.post('/we99/services/rest/createAccount/verify/'+$scope.uuid, $scope.user);
      promise.then(function(resp) {
        $scope.user = resp.data;
      }, function(resp) {
          alert("Unexpected error:"+ resp);
      });
    };

    $scope.activate = function() {
      var promise = $http.post('services/rest/createAccount/verify/'+$scope.uuid, $scope.user);
      promise.then(function() {
        $scope.successTextAlert = "Redirecting to login page...";
        $scope.showSuccessAlert = true;
        $timeout(function() {
          window.location = "login.html";
        }, 3000);
      }, function(resp) {
        if (resp.status === 409) {
          $scope.dangerTextAlert = "Account already active.";
          $scope.showDangerAlert = true;
        } else if (resp.status === 500) {
          $scope.dangerTextAlert = "Error activating. Contact the admin.";
          $scope.showDangerAlert = true;
        } else {
          alert("Unexpected error:"+ resp);
        }
      });
    };

    $scope.sendPasswordReset = function() {
      var promise = $http.post('services/rest/forgotPassword', $scope.user);
      promise.then(function() {
        $scope.successTextAlert = "Check your email for a password reset link.";
        $scope.showSuccessAlert = true;
      }, function(resp) {
        if (resp.status === 500) {
          $scope.dangerTextAlert = "Error resetting. Contact the admin.";
          $scope.showDangerAlert = true;
        } else {
          alert("Unexpected error:"+ resp);
        }
      });
    };

    $scope.loadPWReset = function() {
      var url = new URI();
      var queryParams = url.search(true);
      $scope.user = {};
      $scope.user.email = queryParams['email'];
      $scope.uuid = queryParams['uuid'];

      var promise = $http.post('services/rest/forgotPassword/verify/uuid'+$scope.uuid, $scope.user);
      promise.then(function(resp) {
        $scope.user = resp.data;
      }, function(resp) {
        alert("Unexpected error:"+ resp);
      });
    };

    $scope.activate = function() {
      var promise = $http.post('services/rest/forgotPassword/update/'+$scope.uuid, $scope.user);
      promise.then(function() {
        $scope.successTextAlert = "Password reset, redirecting to login page";
        $scope.showSuccessAlert = true;
        $timeout(function() {
          window.location = "login.html";
        }, 3000);
      }, function(resp) {
          alert("Unexpected error:"+ resp);
      });
    };


    $scope.user = {};
    $scope.user.firstName = "";
    $scope.user.lastName = "";
    $scope.user.email = "";
    $scope.uuid = "";

    $scope.successTextAlert = "Check your email for the activation link.";
    $scope.showSuccessAlert = false;

    $scope.dangerTextAlert = "";
    $scope.showDangerAlert = false;

    // switch flag
    $scope.switchBool = function (value) {
      $scope[value] = !$scope[value];
    };

    /* Validation */
    // REGEX PATTERNS
    $scope.regexNamePattern = /^[a-z]*$/i;
  })
;
