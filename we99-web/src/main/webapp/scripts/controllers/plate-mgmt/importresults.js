'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:AnalysisImportresultsCtrl
 * @description
 * # AnalysisImportresultsCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('ImportResultsCtrl', function ($scope,$upload,$log,RestURLs,RestService,$modalInstance,experiment) {

    //need to display in html modal
    $scope.experiment=experiment;

    $scope.success=false;


    //autoupload watch
  $scope.$watch('files', function () {
    $scope.upload();
  });



    $scope.upload = function () {
      //if(!$scope.selectedPlate){
      //  $scope.errorText="You must select a plate before uploading results";
      //  return;
      //}
      console.log('in upload()');

      if ($scope.files && $scope.files.length) {
        for (var i = 0; i < $scope.files.length; i++) {
          var file = $scope.files[i];
          $upload.upload({
            url: RestURLs.resultsUpload(experiment.id),
            method: "POST",
            file: file
          }).progress(function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            $log.info('progress: ' + progressPercentage + '% ' +
            evt.config.file.name);
          }).success(function (resp) {
            $log.info('file uploaded successfully. Response: ' +resp);
            $scope.modalResponseMsg={type: "success", text: 'Successfully uploaded plate set results!'};
            $scope.success=true;
          }).error(function (resp) {
            $log.error('error in file uploaded. Response: ' +resp);
            $scope.modalResponseMsg={type: "danger", text: 'Error: Failed to parse csv file for results data!'};
            $scope.success=false;
          })

          ;
        }
      }
    };

    $scope.closeAlert=function(){
      $scope.modalResponseMsg=null;
    };


    //closes modal. checks for success so parent controller knows whether to refresh or not
    $scope.dismiss = function () {
      if($scope.success)
        $modalInstance.close('success');


      $modalInstance.dismiss('cancel');
    };

  });
