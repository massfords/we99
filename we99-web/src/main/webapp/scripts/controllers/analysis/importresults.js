'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:AnalysisImportresultsCtrl
 * @description
 * # AnalysisImportresultsCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('ImportResultsCtrl', function ($scope,$upload,RestURLs,RestService,$modalInstance,experiment,plate) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];

    $scope.experiment=experiment;
    $scope.plate=plate;
    //nonsense
    $scope.allPlates=[];
    $scope.inhibitionValues=[10,20,30,40,50];
    $scope.calculationValues=['EC50','Heatmap','All'];


    if($scope.currentExperiment)
      RestService.getExperimentPlates($scope.currentExperiment.id)
        .success(function(resp){
          $scope.allPlates=resp.values;
        })
        .error(function(resp){
          $scope.errorText="Couldn't load plates for experiment.";
        });

    //autoupload watch
  $scope.$watch('files', function () {
    $scope.upload($scope.files);
  });



    $scope.upload = function () {
      //if(!$scope.selectedPlate){
      //  $scope.errorText="You must select a plate before uploading results";
      //  return;
      //}

      if ($scope.files && $scope.files.length) {
        for (var i = 0; i < $scope.files.length; i++) {
          var file = $scope.files[i];
          $upload.upload({
            url: RestURLs.resultsUpload($scope.experiment.id,plate.id),
            method: "POST",
            file: file
          }).progress(function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            console.log('progress: ' + progressPercentage + '% ' +
            evt.config.file.name);
          }).success(function (data, status, headers, config) {
            var result=JSON.stringify(data);
            console.log('file ' + config.file.name + 'uploaded. Response: ' +
            result);
            $scope.uploadOutput='Success: '+result;
          }).error(function (data, status, headers, config) {
            var result=JSON.stringify(data);
            console.log('error in file ' + config.file.name + 'uploaded. Response: ' +
            result);
            $scope.uploadOutput='Error: '+result;
          })

          ;
        }
      }
    };

    $scope.dismiss = function () {
      $modalInstance.dismiss('cancel');
    };

  });
