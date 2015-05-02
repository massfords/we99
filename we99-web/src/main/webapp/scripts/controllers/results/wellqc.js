'use strict';


/**
 * @ngdoc function
 * @name we99App.controller:WellQcCntrl
 * @description
 *
 * # HeatmapCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('WellQcCntrl', ["$q", "$scope", "RestService","TourConstants", function ($q, $scope, RestService,TourConstants) {

    var v = new DataVis();
    var displayBoxLocation = "#scatter-plot";

    function transform(data){
      var result = [];
      data.forEach(function(plate){
        plate.data.forEach(function(well) {
          if (well.wellType === 'NEG_CONTROL' || well.wellType === "POS_CONTROL")
          {
            result.push(well);
          }
        });
      });
      return result;
    }

    // Retrieve list of experiments
    RestService.getExperiments()
      .success(function (response) {

        // Experiment Drop Down
        $scope.experiments = response.values;
        $scope.selectedExperiment = $scope.experiments[0];

        $scope.$watch('selectedExperiment', function(newValue, oldValue) {

            var experimentId = newValue.id;

            RestService.getExperimentPlates(experimentId)
              .success(function(response){
                var plateIds = response.values.filter(function(plate){ return plate.hasResults;}).map(function(plate){return plate.id;});
                var promises = plateIds.map(function(plateId){ return RestService.getPlateResults(experimentId, plateId); });
                $q.all(promises).then(function(response){
                  $scope.data = transform(v.convertPlateResultData(response.map(function(d){return d.data;})));
                  fullDisplayRefresh();
                });
              }).error(function(response){
                $scope.errorText="Could not retrieve plate list for expriement [id=" + experimentId + "]";
              });

        });


      });

    function fullDisplayRefresh(){
      var svg = d3.select(displayBoxLocation).html("");
      v.renderScatterPlot({
        location: displayBoxLocation,
        data: $scope.data,
        onCellClick: function(d) {
            $scope.data.forEach(function(dataPoint){
              if(dataPoint.wellIndex == d.wellIndex){
                dataPoint.included = !dataPoint.included;
              }
            });
          fullDisplayRefresh();
        },
        axisTitle:{
          x: "Time",
          y: "Result Value"
        }
      });

    }

    //=== Tour Settings ===

    $scope.startTour=function(){
      $scope.tourConfig=TourConstants.cleanTourConfig(TourConstants.wellQCTour);
      if($scope.tourConfig.length>=1)
        $scope.startJoyRide=true;
    };

  }]);
