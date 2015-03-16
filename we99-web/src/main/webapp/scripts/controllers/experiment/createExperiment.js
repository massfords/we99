'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:ExperimentCreateCtrl
 * @description
 * # ExperimentCreateCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('ExperimentCreateCtrl', function ($scope, RestService) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
    
    $scope.newExp={};
    
    RestService.getUsers()
	    .success(function(response){
	    	$scope.availUsers=response.values;
	    	$scope.displayAvailUsers=[].concat($scope.availUsers);
	    	
	    	$scope.assignedUsers=[];
	    	$scope.displayAssignedUsers=[];
	    })
	    .error(function(response){
	    	alert('Failed to load users');
	    });
    
    RestService.getProtocols()
    .success(function(response){
    	$scope.protocolValues=response.values;
    })
    .error(function(response){
    	alert('Failed to load protocol');
    });
    
    $scope.createExp=function(){
    	if($scope.assignedUsers.length<=0){
    		$scope.errorText="Experiments must have at least one assigned user"
    		return;
    	}
    	$scope.newExp.assignedUsers=$scope.assignedUsers;
    	
    	
    	RestService.createExperiment($scope.newExp)
    		.error(function(resp){
    			alert('Error: could not create experiment');
    		});
    }
    
    $scope.assignUser=function(){
    	for(var i=0;i<$scope.availUsers.length;i++){
    		if($scope.availUsers[i].isSelected){
    			//console.log('got one! '+i);
    			var movedObj=$scope.availUsers.splice(i,1)[0];
    			$scope.assignedUsers.push(movedObj);
    			break;
    		}
    	}
    }
    
    $scope.removeUser=function(){
    	for(var i=0;i<$scope.assignedUsers.length;i++){
    		if($scope.assignedUsers[i].isSelected){
    			//console.log('got one! '+i);
    			var movedObj=$scope.assignedUsers.splice(i,1)[0];
    			$scope.availUsers.push(movedObj);
    			break;
    		}
    	}
    }
  });
