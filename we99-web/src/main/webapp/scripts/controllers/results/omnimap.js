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
  .controller('OmniMapCtrl', ["$scope", "RestService", function ($scope, RestService) {

    var v = new DataVis();

    var renderBox = null;

    var SCROLL = "SCROLL";
    var ZOOM = "ZOOM";
    var NONE = "NONE";

    var state = NONE;

    // Links to display elements.
    var displayBoxLocation = "#display-box";

    // Retrieve list of experiments
    RestService.getExperiments()
      .success(function(response){

        // Experiment Drop Down
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

          // Data Set
          $scope.data = transform(v.getDummmyPlateData(newValue.id).dataSets);

          console.log($scope.data);

          // Coloration change watcher.
          $scope.$watch('coloring.colorOption', function(newValue, oldValue){ fullDisplayRefresh() });

          renderBox = null;
          fullDisplayRefresh();


        });

        $scope.resetScale = function(){
          renderBox = null;
          fullDisplayRefresh();
        }

      })
      .error(function(response){
        $scope.errorText="Could not retrieve experiments list.";
      });

    /**
     * ###################################
     * Helper Functions
     * ###################################
     */

    function mouseDown(a){
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


    }

    function mouseUp(a){

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

      if( (renderBox.row.two - renderBox.row.one) > 8 &
        (renderBox.col.two - renderBox.col.one) > 8 ) {
        renderMap();
      }else{
        renderBox = null;
      }


    }

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

      var displayBox = d3.select("#display-box")
        .on("mousedown", function(){
          var p = d3.mouse( this);

          displayBox.append( "rect")
            .attr({
              rx      : 6,
              ry      : 6,
              class   : "selection",
              x       : p[0],
              y       : p[1],
              width   : 0,
              height  : 0
            })
        })
        .on("mousemove", function(d){
          var s = d3.select( "rect.selection");

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
        })
        .on("mouseup", function(d){
          displayBox.select( ".selection").remove();
        });



      displayBox.html("");

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

      var bounds = v.renderSingleHeatMap({
        location: "#display-box",
        data: wellValues,
        colorScale: $scope.coloring.colorScale,
        mapFormat: {
          fixedsize_x: 900
        },
        onMouseDown: mouseDown,
        onMouseUp: mouseUp
      });
      // Scale display box.
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
            col: i
          });
        });

        row ++;
      });
      return newData;
    }

  }]


);
