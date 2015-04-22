'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:ExperimentCtrl
 * @description
 * # ExperimentCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('ExperimentListCtrl', ['$scope','$rootScope','$location', 'RestService','$modal',function ($scope,$rootScope,$location,RestService,$modal) {


    //retrieve list of experiments
    function refreshExperiments(){
      RestService.getExperiments()
        .success(function(response){
          $scope.experiments=response.values;
          $scope.displayExperiments=[].concat($scope.experiments);
          })
        .error(function(response){
          $scope.errorText="Could not retrieve experiments list.";
          });
    };

    refreshExperiments();

    $scope.editRow=function(){
      for(var i=0;i<$scope.experiments.length;i++)
        if ($scope.experiments[i].isSelected) {
          $location.path('/experiment/addedit/' + $scope.experiments[i].id);
          break;
      }
    };

    // Deletes an experiment from database
    $scope.removeItem=function(row){
      var confirmed = confirm('Delete \'' + row.name + '\'?');
      if (confirmed) {
        RestService.deleteExperiment(row.id)
          .success(function (response) {
            for (var i = 0; i < $scope.experiments.length; i++)
              if ($scope.experiments[i].id === row.id) {
                $scope.experiments.splice(i, 1);
                break;
              }
          })
          .error(function (response) {
            console.log('Error: ' + response);
          });
      }
    };

    // publish an experiment
    $scope.publish = function(experiment) {
      var confirmed = confirm('Publish '+experiment.name+'?\n' +
                              'This will lock down the experiment data.');
      if (confirmed) {
        RestService.publishExperiment(experiment.id).then(function success() {
          experiment.status = 'PUBLISHED';
        }, function fail(err) {
          console.error(err);
          alert('Could not publish.');
        });
      }
    };

    // fired when table rows are selected
    $scope.$watch('displayExperiments', function(newVal) {
      if(newVal){
        var list=newVal.filter(function(item) {
          return item.isSelected;
        });
        if(list.length==0)
          $rootScope.currentExperiment=null;
        else
          $rootScope.currentExperiment=list[0];
      }

    }, true);

    $scope.uploadFile=function(row){
        var modalInstance = $modal.open({
          backdrop: true,
          size: 'lg',
          templateUrl: 'views/plate-analysis/importresults.html',
          controller: 'ImportResultsCtrl',
          resolve: {
            experiment: function () {
              return row;
            }
          }
        });
        modalInstance.result.then(function (returnVal) {
          $scope.refreshPlates(); // Refreshes plate  when add result screen closed
        });

    };

    $scope.dismiss=function(type){
    	if(type==='info'){
    		$scope.infoText=null;
    	}
    	else if(type==='error')
    		$scope.errorText=null;
    }

    // Tour Settings:

    $scope.startTour=function(){
      $scope.startJoyRide=true;
    }

    $scope.config = [
      {
        type: "title",
        heading: "Welcome to the West-East 99 tour",
        text: '<div class="row"><div id="title-text" class="col-md-12">'
        +'<span class="main-text">Welcome to <strong>West-East 99 Application Tour</strong></span>'
        +'<br>This tour will walk you through the features of our application.</div></div>'

      },{
        type: "element",
        selector: "#expTable",
        heading: "Manage Experiments",
        text: "You can see your current list of experiments in this table",
        placement: "bottom",
        scroll: true
      },{
        type: "element",
        selector: "#searchbar",
        heading: "Manage Experiments",
        text: "Use the searchbar to filter for specific experiments and click the column headers to sort the table",
        placement: "bottom",
        scroll: true
      },
      {
        type: "element",
        selector: "#btnPanel",
        heading: "Manage Experiments",
        text: "You can add new experiments and edit existing ones using these buttons",
        placement: "top",
        scroll: true
      },{
        type: "location_change",
        path: "/experiment/addedit/new"
      },{
        type: "element",
        selector: "#newExpForm",
        heading: "Manage Experiments",
        text: "Use this form to enter information about a new experiment",
        placement: "top",
        scroll: true
      },{
        type: "element",
        selector: "#btnPanel",
        heading: "Manage Experiments",
        text: "You can assign and remove users to this experiment with these buttons",
        placement: "top",
        scroll: true
      },
      {
        type: "element",
        selector: "#saveBtn",
        heading: "Manage Experiments",
        text: "When done entering information for the new experiment, save by clicking this button",
        placement: "top",
        scroll: true
      }

    ];

  }]);
