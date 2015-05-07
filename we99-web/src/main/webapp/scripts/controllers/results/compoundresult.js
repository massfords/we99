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

    // Retrieve list of assays
    RestService.getExperiments()
      .success(function (response) {


        // Assay Drop Down
        $scope.experiments = response.values;
        $scope.selectedExperiment = $scope.experiments[0];

        // Watch the selected assay for change.
        $scope.$watch('selectedExperiment', function(experiment) {

          // Update the data based on the selected assay.]
          RestService.getDoseResponseData(experiment.id)
            .success(function(response){

              $scope.compounds = v.convertDoseResponseData(response.values);
              $scope.selectedCompounds = [$scope.compounds.filter(function(d){return d.hasCurve;})[0]];

              $scope.renderModes = ["TILED", "SINGLE"];
              $scope.renderMode = "SINGLE";
              $scope.$watch('renderMode', function() {
                fullDisplayRefresh();
              });

              /**
               * Toggle compound function.
               * @param compound The compound to be toggled.
               */
              $scope.toggleCompound = function(compound){

                if($scope.selectedCompounds.length < 9){

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
                }

              };


            }).error(function(){
              $scope.errorText='Failed to load dose response data.';
            });


        });

      }).error(function(){
        $scope.errorText='Failed to load assay data.';
      });

    function fullDisplayRefresh(){

      // Display Reset
      d3.select(".coloration").attr("fill","white");
      d3.select("#compound-display-container").html("");

      // Setup for single and tiled mode.
      var displayBoxLocation = null;
      switch($scope.renderMode){
        case "SINGLE":
          d3.select("#compound-display-container")
            .html('<svg style="width: 600px; height: 600px" id="composite-scatter-plot"></svg>');
          displayBoxLocation="#composite-scatter-plot";
          break;
        case "TILED":
          displayBoxLocation="#compound-display-container";
          break;
      }

      var counter = 0;

      // Draw each selected compound.
      console.log($scope.selectedCompounds);

      var scaleY = {
        min: d3.min($scope.selectedCompounds.map(function (d) { return d.MIN; })),
        max: d3.max($scope.selectedCompounds.map(function (d) { return d.MAX; }))
      };


      $scope.selectedCompounds.forEach(function(compound, i){

        var data = compound.curve
                    .map(function(d){ return [d.x, d.y] });

        switch($scope.renderMode){
          case "TILED":

            var tileLoc = "tile-" + counter;

            d3.select(displayBoxLocation)
              .append("svg")
              .attr("width", 200)
              .attr("height", 200)
              .attr("id", tileLoc);

            v.renderLine({
              hasAxis: true, // only generate the axis on the first iteration.
              width: 200,
              height: 200,
              location: "#" + tileLoc,
              scaleY: scaleY,
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
            counter++;
            break;
          case "SINGLE":
                v.renderLine({
                  hasAxis: (i === 0), // only generate the axis on the first iteration.
                  width: 600,
                  height: 600,
                  location: displayBoxLocation,
                  scaleY: scaleY,
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
                break;
        }

        d3.select("#" + compound.compound).attr("fill", colors(i));

      });
    }

    //=== Tour Settings ===

    $scope.startTour=function(){
      $scope.tourConfig=TourConstants.cleanTourConfig(TourConstants.compoundResultsTour);
      if($scope.tourConfig.length>=1)
        $scope.startJoyRide=true;
    };


  }]);
