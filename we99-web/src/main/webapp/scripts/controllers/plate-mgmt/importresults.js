'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:AnalysisImportresultsCtrl
 * @description
 * # AnalysisImportresultsCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('ImportResultsCtrl', function ($scope,$upload,$log,RestURLs,RestService,$modalInstance,experiment,plate) {

    //need to display in html modal
    $scope.experiment=experiment;
    $scope.success=false;

    $scope.title="Import Single Plate Results";
    if(!plate)
      $scope.title="Import Bulk Plate Results";
    else
      $scope.plate=plate;




    //autoupload watch
  $scope.$watch('files', function () {
    $scope.upload();
  });

    // Figure out right url for either single plate or bulk
    function getResultsUrl(){
      if($scope.plate)
        return RestURLs.resultsUploadPerPlate(experiment.id,plate.id);
      else
        RestURLs.resultsUpload(experiment.id);
    }



    $scope.upload = function () {

      if ($scope.files && $scope.files.length) {
        for (var i = 0; i < $scope.files.length; i++) {
          var file = $scope.files[i];
          $upload.upload({
            url: getResultsUrl(),
            method: "POST",
            file: file
          }).progress(function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            $log.info('progress: ' + progressPercentage + '% ' +
            evt.config.file.name);
          }).success(function (resp) {
            $log.info('file uploaded successfully. Response: ' +resp);
            $scope.modalResponseMsg={type: "success", text: 'Successfully uploaded plate results!'};
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
