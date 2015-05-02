'use strict';


/**
 * @ngdoc function
 * @name we99App.controller:DoseResponseCntrl
 * @description
 *
 * # DoseResponseCntrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('DoseResponseCntrl', ["$q", "$scope", "RestService","TourConstants",
    function ($q, $scope, RestService,TourConstants) {

    var v = new DataVis();
    var displayBoxLocation = "#scatter-plot";

    function transform(data){
      var result = [];
      data.forEach(function(plate){
        plate.data.forEach(function(well) {
          if (well.wellType !== 'NEG_CONTROL' & well.wellType !== "POS_CONTROL") {

            var found = false;

            result.forEach(function (r) {
              if (r.compound === well.compound) {
                found = true;
                r.wells.push(well);
              }
            });

            if(!found){
              result.push(
                {compound: well.compound, wells: []}
              );
            }

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
        console.log(response.values);
        $scope.selectedExperiment = $scope.experiments[0];

        $scope.$watch('selectedExperiment', function(newValue, oldValue) {

          var experimentId = newValue.id;

          RestService.getDoseResponseData(experimentId)
            .success(function(response){

              $scope.compounds = v.convertDoseResponseData(response.values);
              $scope.selectedCompound = $scope.compounds[0];

              $scope.selectCompound = function(compound){
                $scope.selectedCompound = compound;
                fullDisplayRefresh();
              }

              fullDisplayRefresh();

            }).error(function(error){

            });

        });


      });

    function fullDisplayRefresh(){
      d3.select(displayBoxLocation).html("");


      console.log($scope.selectedCompound.curve);

      var min = d3.min(
        $scope.selectedCompound.wells.map(function(d) {return d.value;})
      );
      var max = d3.max($scope.selectedCompound.wells.map(function(d) {return d.value;}));

      var hasCurve = $scope.selectedCompound.hasCurve;

      var onClick = function(d) {
        console.log(d);

        var toggle = null;
        $scope.selectedCompound.wells.forEach(function(dataPoint){
          if(dataPoint.wellIndex == d.wellIndex){
            dataPoint.included = !dataPoint.included;
            toggle = dataPoint.included;
          }
        });

        function toString(d){
          if(d){
            return "INCLUDED";
          }else{
            return "EXCLUDED";
          }
        }

        RestService.updateDoseResponseResult($scope.selectedExperiment.id, {
          doseId: d.wellIndex,
          plateId: d.plateId,
          status: toString(toggle)
        }).success(function(d){
          var data = v.convertDoseResponseData([d])[0];
          for(var i = 0; i<$scope.compounds.length; i++){
            if($scope.compounds[i].compound === data.compound){
              $scope.compounds[i = data];
            }
          }
          $scope.selectedCompound = data;
          fullDisplayRefresh();
          console.log(data);
        });

      };

      if(hasCurve){
        v.renderScatterPlot({
          location: displayBoxLocation,
          data: $scope.selectedCompound.wells,
          xScaleIsDate: false,
          onCellClick: onClick,
          axisTitle:{
            x: "Dose",
            y: "Response"
          },
          scaleY: {min: min, max: max},
          linePoints: $scope.selectedCompound.curve
        });
      }else{
        v.renderScatterPlot({
          location: displayBoxLocation,
          data: $scope.selectedCompound.wells,
          xScaleIsDate: false,
          onCellClick: onClick,
          axisTitle:{
            x: "Dose",
            y: "Response"
          },
          scaleY: {min: min, max: max}
        });
      }
    }
      //=== Tour Settings ===

      $scope.startTour=function(){
        $scope.tourConfig=TourConstants.cleanTourConfig(TourConstants.doseResponseTour);
        if($scope.tourConfig.length>=1)
          $scope.startJoyRide=true;
      };

  }]);
