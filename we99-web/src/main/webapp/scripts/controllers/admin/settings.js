'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:AdminSettingsCtrl
 * @description
 * # AdminSettingsCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('AdminSettingsCtrl', function ($scope,RestService) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  
    // load config settings
    RestService.getServerSettings()
    	.success(function(resp){
    		$scope.configSettings=resp;
    	})
    	.error(function(resp){
    		setError('Could not load server settings.');
    	});
    
    // load email filter settings
    RestService.getEmailFilter()
    	.success(function(resp){
    		$scope.emailFilter=resp;
    	})
    	.error(function(resp){
    		setError('Could not load server settings.');
    	});
    
    
    //save server settings
    $scope.saveServerSettings=function(){
    	RestService.updateServerSettings($scope.configSettings)
	    	.success(function(){
	    		setInfo('Saved new server email settings.');
	    	})
	    	.error(function(){
	    		setError('Could not save email server settings.');
	    	});
    }
    
    //save email filter settings
    $scope.saveEmailFilter=function(){
    	RestService.updateEmailFilter($scope.emailFilter)
	    	.success(function(){
	    		setInfo('Saved email filter settings.');
	    	})
	    	.error(function(){
	    		setError('Could not save email filter settings.');
	    	});
    }
    
    function setInfo(msg){
    	$scope.infoText=msg;
    	$scope.errorText=null;
    };
    
    function setError(msg){
    	$scope.infoText=null;
    	$scope.errorText=msg;
    	console.log(msg);
    };
   
    
  });
