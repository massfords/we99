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
  .controller('CompoundResultsCntrl', ["$q", "$scope", "RestService", function ($q , $scope, RestService) {

    var v = new DataVis();
    var displayBoxLocation = "#scatter-plot";

    function transform(data){
      var result = [];
      data.forEach(function(plate){
        plate.data.forEach(function(well) {
          if (well.wellType !== 'NEG_CONTROL' & well.wellType !== "POS_CONTROL" & well.included) {

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

      d3.select(".coloration").attr("fill","white");
      d3.select(displayBoxLocation).html("");

      var colors = d3.scale.category20();

      $scope.selectedCompounds.forEach(function(compound, i){

        var color = colors(i);
        var data = [];
        compound.wells.forEach(function(d){
          if(d.included){
            data.push( [d.amount, d.value] );
          }
        });

        var scaleY = {
          min: d3.min(data.map(function (d) { return d[1]; })),
          max: d3.max(data.map(function (d) { return d[1]; }))
        };

        var scaleX = {
          min: d3.min(data.map(function (d) { return d[0]; })),
          max: d3.max(data.map(function (d) { return d[0]; }))
        };



        if(i === 0) {
          v.renderLine({
            hasAxis: true,
            width: 600,
            height: 600,
            location: displayBoxLocation,
            scaleY: scaleY,
            scaleX: scaleX,
            linePoints: compound.curve,
            axisTitle: {
              x: "Dose",
              y: "Response"
            },
            color: color
          });
        }else{
          v.renderLine({
            hasAxis: false,
            width: 600,
            height: 600,
            location: displayBoxLocation,
            scaleY: scaleY,
            scaleX: scaleX,
            linePoints: compound.curve,
            axisTitle: {
              x: "Dose",
              y: "Response"
            },
            color: color
          });
        }

        d3.select("#" + compound.compound)
          .attr("fill", color);

      });

    }

  }]);
