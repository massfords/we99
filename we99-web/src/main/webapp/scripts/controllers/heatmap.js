'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:HeatmapCtrl
 * @description
 * # HeatmapCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('HeatmapCtrl', ["$scope", "RestService", function ($scope, RestService) {


    // Default Data
    var colName = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'],
      rowName = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'],
      resultOptions = ['Sodium (Na)', 'Potassium (K)', 'Urea', 'Glucose (fasting)'];


    var allDataSets = [];
    for (var opt = 0; opt < resultOptions.length; opt++) {
      var dataSet = [];
      for (var num = 0; num < 15; num++) {
        var array = [];
        var index = 0;
        for (var i = 0; i < colName.length; i++) {
          for (var j = 0; j < rowName.length; j++) {
            array.push({wellIndex: index++, row: i, col: j, value: Math.random(), included: true});
          }
        }
        dataSet[num] = array;
      }
      allDataSets[opt] = dataSet;
    }

    var sampleJsonData = {
      resultOptions: resultOptions,
      dataSets: allDataSets.map(function (dataSet) {
        return  dataSet.map(function (d) {
            return {
              colName: colName,
              rowName: rowName,
              data: d,
              name: "A" + String(Math.random())
            };
          })
      })
    };


    // Start of real controller stuff.
    var heatmaps = new function(){

      var self = this;

      var itemSize = 9,
        cellSize = itemSize-1,
        windowHeight = 500,
        margin = {top:20,right:20,bottom:20,left:25};

      var listSvg = d3.select("#list-box")
        .append("svg")
        .attr("width", 100)
        .attr("height", windowHeight);

      var displaySvg = d3.select('#display-box')
        .append('svg')
        .attr("width", 600)
        .attr("height", windowHeight);

      var colorScale = null;

      self.renderOne = function(d){

        displaySvg.html("");
        var myData = d;
        var rect =  displaySvg
          .append('g')
          .selectAll('rect')
          .data(d.data)
          .enter()
          .append('rect')
          .attr('width',cellSize * 3)
          .attr('height',cellSize * 3)
          .attr('x',function(d) {return 10 + (d.col * itemSize * 3) + 1; })
          .attr('y',function(d) {return 10 + (d.row * itemSize * 3) + 1; })
          .attr('fill',function(d) {return colorScale(d.value); })
          .style("stroke", function(d) { if(d.included) return 'black'; else return 'red';})
          .on('click', function(d) {
            d.included = !d.included;
            self.renderOne(myData);
          });

        displaySvg.selectAll('text')
          .data( [d] )
          .enter()
          .append('text')
          .attr('x', '10')
          .attr('y', '300' )
          .text( function(d) {return d.name ;} );
      };

      self.renderThumbnails = function(data){

        listSvg.html("");

        // Compute Legend Values
        var nums = [];
        data.forEach(function(arr) {
         arr.data.forEach(function(d){ nums.push(d.value); });
        });

        var min = d3.min(nums);

        var max = d3.max(nums);

        colorScale = d3.scale.linear()
          .range(['lightgreen', 'darkgreen'])
          .domain([min, max]);

        var count = 0;
        data.forEach(function(myData){

          var height = 11 * cellSize,
              width = 11 * cellSize;

          var heatmap = listSvg
            .append('g')
            .attr('width', width)
            .attr('height', height);

          var ty = (height + 35) * count;

          heatmap.on("click", change);
          function change() {
            self.renderOne(myData);
          }

          var rect = heatmap.selectAll('rect')
            .data(myData.data)
            .enter()
            .append('rect')
            .attr('width',cellSize)
            .attr('height',cellSize)
            .attr('x',function(d) {return (d.col * itemSize) + 5 ; })
            .attr('y',function(d) {return ( ty ) + (d.row * itemSize) + 20; })
            .attr('fill',function(d) {return colorScale(d.value); });

          heatmap.append('text')
            .attr('x', '1')
            .attr('y', ty + (cellSize * 11 ) + 35 )
            .text( myData.name );

          count = count + 1;

        });

        listSvg.attr("height", ( count * cellSize * 11) + 35 );
      }
    };

    // Controller Code
    $scope.resultOptions = sampleJsonData.resultOptions.map(function(d,i){
      return {label: d, value: i};
    });
    $scope.result = $scope.resultOptions[0];

    $scope.$watch('result', function(newValue, oldValue) {
      heatmaps.renderThumbnails(sampleJsonData.dataSets[newValue.value]);
    });

  }]
);
