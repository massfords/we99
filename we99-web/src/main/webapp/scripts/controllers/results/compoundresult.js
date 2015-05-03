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
    function ($q , $scope, RestService, TourConstants) {

    var v = new DataVis();

    // Color scale.
    var colors = d3.scale.category20();


    // Retrieve list of experiments
    RestService.getExperiments()
      .success(function (response) {

        $scope.isLoading = true;

        // Experiment Drop Down
        $scope.experiments = response.values;
        $scope.selectedExperiment = $scope.experiments[0];

        // Watch the selected experiment for change.
        $scope.$watch('selectedExperiment', function(experiment) {

          $scope.isLoading = true;

          // Update the data based on the selected experiment.
          RestService.getDoseResponseData(experiment.id)
            .success(function(response){

              $scope.compounds = v.convertDoseResponseData(response.values);
              $scope.selectedCompounds = [$scope.compounds.filter(function(d){return d.hasCurve;})[0]];

              /**
               * Toggle compound function.
               * @param compound The compound to be toggled.
               */
              $scope.toggleCompound = function(compound){

                if(compound.hasCurve) {
                  // Determine if the compound is already selected.
                  var index = -1;
                  $scope.selectedCompounds.forEach(function (d, i) {
                    if (compound.$$hashKey === d.$$hashKey) {
                      index = i;
                    }
                  });

                  // If it is remove it.
                  if (index > -1) {
                    $scope.selectedCompounds.splice(index, 1);
                  } else {
                    $scope.selectedCompounds.push(compound);
                  }

                  // Full display refresh doesn't handle the coloration
                  // of the toggled compound correctly.
                  d3.select("#" + compound.compound)
                    .attr("fill", "white");

                  fullDisplayRefresh();
                }

              };

              fullDisplayRefresh();

            }).error(function(){
              $scope.errorText='Failed to load dose response data.';
            });


        });

      }).error(function(){
        $scope.errorText='Failed to load experiment data.';
      });;

    function fullDisplayRefresh(){

      var displayBoxLocation = "#scatter-plot";

      // Display reset.
      d3.select(".coloration").attr("fill","white");
      d3.select(displayBoxLocation).html("");

      // Draw each selected compound.
      $scope.selectedCompounds.forEach(function(compound, i){

        var data = compound.wells
                    .filter(function(d){return d.included;})
                    .map(function(d){ return [d.amount, d.value] });

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
