'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:ExperimentCreateCtrl
 * @description
 * # ExperimentCreateCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('ExperimentCreateCtrl', function ($scope,$routeParams, $location, $modal, RestService) {

    // if given id, this is an edit of an existing experiment
    if ($routeParams.addeditId==='new') {
      $scope.editMode = false;
      $scope.pageTitle = 'Create New Experiment';
      $scope.newExp = {};
      refreshProtocolList();
      refreshUsersList();
    } else {
      $scope.editMode = true;
      $scope.pageTitle = 'Modify Experiment';
      RestService.getExperimentById($routeParams.addeditId)
        .success(function (resp) {
          $scope.newExp = resp;
          refreshProtocolList();
          refreshUsersList();
        })
    }

    // retrieves user list from db
    function refreshUsersList(){

      if($scope.newExp.id){
        //get assigned users and all users to populate list
        RestService.getExperimentMembers($scope.newExp.id)
          .success(function(resp){
            $scope.assignedUsers=resp.values;
            $scope.displayAssignedUsers=[].concat($scope.assignedUsers);

            RestService.getUsers().success(function(response){
              var allUsers=response.values;
              $scope.availUsers=[];
              //find users that haven't already been assigned
              for(var i=0;i<allUsers.length;i++){
                var unique=true;
                for (var j = 0; j < $scope.assignedUsers.length; j++)
                  if ($scope.assignedUsers[j].id === allUsers[i].id){
                    unique=false;
                    break;
                    }
                if(unique)
                  $scope.availUsers.push(allUsers[i]);
              }
              $scope.displayAvailUsers=[].concat($scope.availUsers);

            })
              .error(function(response){
                $scope.errorText='Failed to load available users';
              });
          })
          .error(function(response){
            $scope.errorText='Failed to load current experiment users';
          });
          // get current user too
          RestService.getCurrentUser()
              .success(function(resp){
                  $scope.currentUser=resp;
              });
      }
      else{
        //automatically assign current user
        RestService.getCurrentUser()
          .success(function(resp){
            $scope.currentUser=resp;
            RestService.getUsers().success(function(response){
              $scope.availUsers=response.values;
              var index=$scope.availUsers.indexOf($scope.currentUser);
              $scope.availUsers.splice(index,1);
              $scope.displayAvailUsers=[].concat($scope.availUsers);

              $scope.assignedUsers=[$scope.currentUser];
              $scope.displayAssignedUsers=[].concat($scope.assignedUsers);
            })
              .error(function(response){
                $scope.errorText='Failed to load users';
              });
          })
          .error(function(response){
            $scope.errorText='Failed to load current user info';
          });
      }

    }

    // retrieves protocol list from db
    function refreshProtocolList(){
	    RestService.getProtocols()
	    .success(function(response){
	    	$scope.protocolValues=response.values;
          if($scope.editMode===true)
            $scope.newExp.protocol = selectMatching($scope.protocolValues, "id", $scope.newExp.protocol.id);
	    })
	    .error(function(response){
	    	$scope.errorText='Failed to load protocol list';
	    });
    }

    /**
     * walks the array and selects the first object with a property that matches
     * the value given.
     * @param input
     * @param key
     * @param value
     * @returns {*}
     */
    function selectMatching(input, key, value) {
      for(var i=0; i<input.length; i++) {
        if (input[i][key] === value) {
          return input[i];
        }
      }
    }


    function saveMembers(expId,memberIds){
      RestService.setExperimentMembers(expId, memberIds)
        .success(function (resp) {
          $location.path('/experiment')
        })
        .error(function (resp) {
          $scope.errorText = 'Error: could not assign members to new experiment';
        });
    }

    $scope.saveExp=function(){
    	if($scope.assignedUsers.length<=0){
    		$scope.errorText="Experiments must have at least one assigned user";
    		return;
    	} else if ($scope.newExp.status === "PUBLISHED") {
        $scope.errorText="Published experiments cannot be modified";
        return;
      }
    	//$scope.newExp.assignedUsers=$scope.assignedUsers;
      var memberIds=[];
      for(var i=0;i<$scope.assignedUsers.length;i++){
        memberIds.push($scope.assignedUsers[i].id)
      }

    	RestService.createExperiment($scope.newExp)
    		.success(function(resp){
          if (memberIds.length != 0) {
            saveMembers(resp.id,memberIds);
          } else {
            $location.path('/experiment');
          }
    		})
    		.error(function(resp){
    			$scope.errorText='Error: could not save experiment';
    		});
    };

    $scope.assignUser=function(){
    	for(var i=0;i<$scope.availUsers.length;i++){
    		if($scope.availUsers[i].isSelected){
    			//console.log('got one! '+i);
    			var movedObj=$scope.availUsers.splice(i,1)[0];
    			$scope.assignedUsers.push(movedObj);
    			break;
    		}
    	}
    };

    $scope.removeUser=function(){
    	for(var i=0;i<$scope.assignedUsers.length;i++){
    		if($scope.assignedUsers[i].isSelected){
    			//console.log('got one! '+i);
    			if($scope.assignedUsers[i].id===$scope.currentUser.id){
    				$scope.errorText='You cannot remove yourself from an experiment.';
    				break;
    			}
    			else{
	    			var movedObj=$scope.assignedUsers.splice(i,1)[0];
	    			$scope.availUsers.push(movedObj);
	    			break;
    			}
    		}
    	}
    };

    //modal control
    $scope.newProtocol = function () {

        var modalInstance = $modal.open({
          templateUrl: 'views/experiment/addprotocol.html',
          controller: 'AddProtocolCtrl',
          resolve: {
            protocols: function () {
              return $scope.protocolValues;
            }
          }
        });

        //when modal closes...
        modalInstance.result.then(function (newProc) {
        	if(newProc){
        		//console.log('modal rv: '+newProc);
        		$scope.protocolValues.push(newProc);}
        	//refreshProtocolList();
        }, function () {
          console.log('Modal dismissed at: ' + new Date());
        });
      };

  });

///
/// Modal Controller for Add Protocol
///
angular.module('we99App')
.controller('AddProtocolCtrl', function ($scope, RestService, $modalInstance, protocols) {

	  //get all previously created protocols to check for uniqueness
	  $scope.protocols = protocols;


	  $scope.ok = function () {

		//throw error if name is not unique
		for(var i=0;i<protocols.length;i++){
			if(protocols[i].name===$scope.protocol.name){
				$scope.errorText="New protocol name must be unique.";
				return;
			}
		}

		//try to create new protocol with rest service
		RestService.addProtocol($scope.protocol)
			.success(function(resp){
				$modalInstance.close(resp);
			})
			.error(function(resp){
	  			$scope.errorText="Could not save new protocol";});


	  };

	  $scope.cancel = function () {
	    $modalInstance.dismiss('cancel');
	  };
	});
