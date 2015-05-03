'use strict';


/**
 * @ngdoc function
 * @name we99App.controller:CompoundResultsCntrl
 * @description
 *
 * # DoseResponseCntrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('CompoundResultsCntrl', ["$q", "$scope", "RestService", "TourConstants",
    function ($q , $scope, RestService,TourConstants) {

    var v = new DataVis();

    // Color scale.
    var colors = d3.scale.category20();

    // Retrieve list of experiments
    RestService.getExperiments()
      .success(function (response) {

        // Experiment Drop Down
        $scope.experiments = response.values;
        $scope.selectedExperiment = $scope.experiments[0];

        $scope.$watch('selectedExperiment', function(newValue, oldValue) {

          var experimentId = newValue.id;

          RestService.getDoseResponseData(experimentId)
            .success(function(response){

              $scope.compounds = v.convertDoseResponseData(response.values);
              $scope.selectedCompounds = [$scope.compounds[0]];

              $scope.toggleCompound = function(compound){

                var index = -1;

                $scope.selectedCompounds.forEach(function(d,i){
                  if(compound.$$hashKey === d.$$hashKey){
                    index = i;
                  }
                });

                if(index > -1){
                  console.log($scope.selectedCompounds);
                  $scope.selectedCompounds.splice(index, 1);
                  console.log($scope.selectedCompounds);
                }else{
                  $scope.selectedCompounds.push(compound);
                }

                console.log($scope.selectedCompounds);


                d3.select("#" + compound.compound)
                  .attr("fill", "white");

                fullDisplayRefresh();

              };

              fullDisplayRefresh();

            }).error(function(error){

            });


        });

      });

    function fullDisplayRefresh(){

      var displayBoxLocation = "#scatter-plot";

      d3.select(".coloration").attr("fill","white");
      d3.select(displayBoxLocation).html("");

      $scope.selectedCompounds.forEach(function(compound, i){

        var data = [];
        compound.wells.forEach(function(d){
          if(d.included){
            data.push( [d.amount, d.value] );
          }
        });

        v.renderLine({
          hasAxis: (i === 0), // only generate the axis on the first iteration.
          width: 600,
          height: 600,
          location: displayBoxLocation,
          scaleY: {
            min: d3.min(data.map(function (d) { return d[1]; })),
            max: d3.max(data.map(function (d) { return d[1]; }))
          },
          scaleX: {
            min: d3.min(data.map(function (d) { return d[0]; })),
            max: d3.max(data.map(function (d) { return d[0]; }))
          },
          linePoints: compound.curve,
          axisTitle: {
            x: "Dose",
            y: "Response"
          },
          color: colors(i)
        });


        d3.select("#" + compound.compound)
          .attr("fill", colors(i));

      });

    }

    //=== Tour Settings ===

    $scope.startTour=function(){
      $scope.tourConfig=TourConstants.cleanTourConfig(TourConstants.compoundResultsTour);
      if($scope.tourConfig.length>=1)
        $scope.startJoyRide=true;
    };


  }]);
