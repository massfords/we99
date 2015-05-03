'use strict';


/**
 * @ngdoc function
 * @name we99App.controller:HeatmapCtrl
 * @description
 *
 * # HeatmapCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('OmniMapCtrl', ["$q", "$scope", "RestService","TourConstants", function ($q, $scope, RestService,TourConstants) {

    var v = new DataVis();

    var renderBox = null;

    var ZOOM = "ZOOM";

    $scope.state = ZOOM;

    // Links to display elements.
    var displayBoxLocation = "#display-box";




    // Retrieve list of assays
    RestService.getExperiments()
      .success(function(response){

        // Assay Drop Down
        $scope.experiments= response.values;
        $scope.selectedExperiment = $scope.experiments[0];

        // Set up coloration defaults.
        $scope.coloring = {
          colorScale: null,
          min: null,
          max: null,
          colorOptions: [
            {colorName: "Blue Green", colorOne: "#1f78b4", colorTwo: "#b2df8a" },
            {colorName: "Teal Purple", colorOne: "#9900CC", colorTwo: "#99FFEB" },
            {colorName: "Orange Purple", colorOne: "#FF9966", colorTwo: "#9999E6" }
          ],
          colorOption: null
        };
        $scope.coloring.colorOption = $scope.coloring.colorOptions[0];


        $scope.$watch('selectedExperiment', function(newValue, oldValue){

          var experimentId = newValue.id;

          RestService.getExperimentPlates(experimentId)
            .success(function(response){
              var plateIds = response.values.filter(function(plate){ return plate.hasResults;}).map(function(plate){return plate.id;});
              var promises = plateIds.map(function(plateId){ return RestService.getPlateResults(experimentId, plateId); });
              $q.all(promises).then(function(response){
                $scope.data = transform(v.convertPlateResultData(response.map(function(d){return d.data;})));

                // Coloration change watcher.
                $scope.$watch('coloring.colorOption', function(newValue, oldValue){ fullDisplayRefresh() });

                renderBox = null;
                fullDisplayRefresh();

              });
            }).error(function(response){
              $scope.errorText="Could not retrieve plate list for assay [id=" + experimentId + "]";
            });;


        });

        $scope.resetScale = function(){
          renderBox = null;
          fullDisplayRefresh();
        }

      })
      .error(function(response){
        $scope.errorText="Could not retrieve assays list.";
      });

    /**
     * ###################################
     * Helper Functions
     * ###################################
     */


    function fullDisplayRefresh(){
      setUpNewScale();

      // Create a color guide for the heat map.
      v.heatMapColorGuide({
        location: "#gradients",
        colorScale: $scope.coloring.colorScale,
        min: $scope.coloring.min,
        max: $scope.coloring.max
      });

      renderMap();
    }

    function renderMap(){

      // Get display box pointer, and setup the selection box on mouse down/move/up.
      // Based on this sample (almost exactly) http://bl.ocks.org/lgersman/5311083
      var displayBox = d3.select("#display-box")
          .on("mousedown", function(){

            if($scope.state == ZOOM){
              // Setup box.
              var p = d3.mouse(this);
              displayBox.append( "rect")
                .attr({
                  rx      : 6,
                  ry      : 6,
                  class   : "selection",
                  x       : p[0],
                  y       : p[1],
                  width   : 0,
                  height  : 0
                });
            }

          })
          .on("mousemove", function(){

            // Move box as mouse moves.
            var s = d3.select( "rect.selection");

            if($scope.state == ZOOM){
            if( !s.empty()) {
              var p = d3.mouse(this),

                d = {
                  x       : parseInt( s.attr( "x"), 10),
                  y       : parseInt( s.attr( "y"), 10),
                  width   : parseInt( s.attr( "width"), 10),
                  height  : parseInt( s.attr( "height"), 10)
                },
                move = {
                  x : p[0] - d.x,
                  y : p[1] - d.y
                }
                ;

              if( move.x < 1 || (move.x*2<d.width)) {
                d.x = p[0];
                d.width -= move.x;
              } else {
                d.width = move.x;
              }

              if( move.y < 1 || (move.y*2<d.height)) {
                d.y = p[1];
                d.height -= move.y;
              } else {
                d.height = move.y;
              }

              s.attr( d);
            }
          }
        }
      )
          .on("mouseup", function(d){


          if($scope.state == ZOOM) {
            // Move remove box after done.
            displayBox.select(".selection").remove();
          }
          });


      // Clear all current svg (performance heavy)
      displayBox.html("");

      // If render box is populated use it rather then the current set of wells.
      var wellValues = [].concat($scope.data.wellValues);
      if(renderBox) {
        var newWells = [];
        wellValues.forEach(function(d){
          if ( d.row >= renderBox.row.one &
                   d.row <= renderBox.row.two + 1 &
                   d.col >= renderBox.col.one &
                   d.col <= renderBox.col.two + 1 ){

            var newd = $.extend(true, {}, d);;
            newd.row = newd.row - renderBox.row.one;
            newd.col = newd.col - renderBox.col.one;
            newWells.push(newd);
          }
        });
        wellValues = newWells;
      }

      // Render single heat map.
      var bounds = v.renderSingleHeatMap({
        location: "#display-box",
        data: wellValues,
        colorScale: $scope.coloring.colorScale,
        mapFormat: {
          fixedsize_x: 900
        },
        onMouseDown: function(a){
          // First part of render box.
          console.log($scope.state);
          if($scope.state === ZOOM) {
            renderBox = {
              row: {
                one: a.row,
                two: null
              },
              col: {
                one: a.col,
                two: null
              }
            };
          }else{
            $scope.data.wellValues.forEach(function(d){
              if(d.wellIndex === a.wellIndex){
                d.included = !d.included;
              }
            });
            renderMap();
          }
          },
        onMouseUp: function (a){

          if($scope.state === ZOOM) {

            // Add in second part of render box.
            renderBox = {
              row: {
                one: Math.min(a.row, renderBox.row.one),
                two: Math.max(a.row, renderBox.row.one)
              },
              col: {
                one: Math.min(a.col, renderBox.col.one),
                two: Math.max(a.col, renderBox.col.one)
              }
            };

            // Determine if render box is 'big' enough.
            if ((renderBox.row.two - renderBox.row.one) > 2 &
              (renderBox.col.two - renderBox.col.one) > 2) {
              renderMap();
            } else {
              renderBox = null;
            }

          }

        }
      });

      // Scale display box to fix contents.
      displayBox.attr("height", bounds.y + bounds.height);

    }


    /**
     * Upacks the scope values
     */
    function setUpNewScale(){

      var values = $scope.data.wellValues.map(function(well) {
        return well.value;
      });

      var scale = v.colorScale({
        data: values,
        colorOne: $scope.coloring.colorOption.colorOne,
        colorTwo: $scope.coloring.colorOption.colorTwo
      });

      $scope.coloring.colorScale = scale.scale;
      $scope.coloring.min = scale.min;
      $scope.coloring.max = scale.max;
    }


    function transform(data){
      var newData = {
        plateNames: [],
        wellNames: null,
        wellValues: []
      };

      var row = 0;

      data.forEach(function(plate){

        newData.plateNames.push(plate.name);

        if(!newData.wellNames){
          newData.wellNames = plate.data.map(function(d){
            return d.row + "-" + d.col;
          })
        }

        plate.data.forEach(function(d, i) {
          newData.wellValues.push({
            value: d.value,
            wellIndex: d.wellIndex,
            wellType: d.wellType,
            included: d.included,
            row: row,
            col: i,
            plate: d.plate
          });
        });

        row ++;
      });
      return newData;
    }


    //=== Tour Settings ===

    $scope.startTour=function(){
      $scope.tourConfig=TourConstants.cleanTourConfig(TourConstants.omnimapTour);
      if($scope.tourConfig.length>=1)
        $scope.startJoyRide=true;
    };


  }]
);
