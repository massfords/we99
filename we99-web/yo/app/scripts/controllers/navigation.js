//taken from https://spring.io/blog/2015/01/12/the-login-page-angular-js-and-spring-security-part-ii
angular.module('we99App')
.controller('navigation',

  function($rootScope, $scope, $http, $location) {

  var authenticate = function(callback) {

    $http.get('user').success(function(data) {
      if (data.name) {
        $rootScope.authenticated = true;
      } else {
        $rootScope.authenticated = false;
      }
      callback && callback();
    }).error(function() {
      $rootScope.authenticated = false;
      callback && callback();
    });

  }

  authenticate();
  $scope.credentials = {};
  $scope.login = function() {
    $http.post('login', $.param($scope.credentials), {
      headers : {
        "content-type" : "application/x-www-form-urlencoded"
      }
    }).success(function(data) {
      authenticate(function() {
        if ($rootScope.authenticated) {
          $location.path("/");
          $scope.error = false;
        } else {
          $location.path("/login");
          $scope.error = true;
        }
      });
    }).error(function(data) {
      $location.path("/login");
      $scope.error = true;
      $rootScope.authenticated = false;
    })
  };
  
  $scope.logout = function() {
  $http.post('logout', {}).success(function() {
    $rootScope.authenticated = false;
    $location.path("/");
  }).error(function(data) {
    $rootScope.authenticated = false;
  });
}
});