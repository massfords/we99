'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:NavigationCtrl
 * @description
 * # NavigationCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('NavigationCtrl', function($rootScope, $scope, $http, $location) {
    
	 $scope.logout=function(){
		 alert('I dont know how to logout yet');
		 if(false){
			 $http.post('logout', {}).success(function() {
				    $rootScope.authenticated = false;
				    $location.path("/");
				  }).error(function(data) {
				    $rootScope.authenticated = false;
				  });
		 }
	 }


	  var authenticate = function(callback) {

	    $http.get('/services/rest/me').success(function(data) {
	    	console.log(data);
	      if (data) {
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
	});