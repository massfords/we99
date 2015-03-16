'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:PlateMgmtImportplatemapCtrl
 * @description
 * # PlateMgmtImportplatemapCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('PlateMgmtImportplatemapCtrl', function ($scope, $upload,RestURLs) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
    
    $scope.$watch('files', function () {
        $scope.upload($scope.files);
    });

    $scope.upload = function (files) {
        if (files && files.length) {
            for (var i = 0; i < files.length; i++) {
                var file = files[i];
                $upload.upload({
                    url: RestURLs.plateMap,
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

  });
