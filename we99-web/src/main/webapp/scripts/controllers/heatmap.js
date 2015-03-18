'use strict';

/**
 * @ngdoc function
 * @name we99App.controller:HeatmapCtrl
 * @description
 * # HeatmapCtrl
 * Controller of the we99App
 */
angular.module('we99App')
  .controller('HeatmapCtrl', ["$scope", "RestService", "d3Service", function ($scope, RestService, d3Service) {

    // Stores the index of the presently selected value in the display box.
    var selectedIndex = 0;

    // Stores the presently selected result value.
    var resultValueIndex = 0;

    // Stores the data set.
    var dataSetLink = null;

    // Links to display elements.
    var listBox = d3.select("#list-box");
    var displayBox = d3.select("#display-box");


    /**
     * Render the display box.
     *
     * @param data The data to be rendered in the display box.
     */
    function renderDisplayBox(resultValueIndex, selectedIndex){
      displayBox.html("");
      console.log(resultValueIndex, selectedIndex);
      d3Service.renderSingleHeatMap({
        location: "#display-box",
        data: dataSetLink[resultValueIndex][selectedIndex].data,
        title: dataSetLink[resultValueIndex][selectedIndex].name,
        onCellClick: function(d) {
          dataSetLink[resultValueIndex][selectedIndex].data[d.wellIndex].included =
            !dataSetLink[resultValueIndex][selectedIndex].data[d.wellIndex].included;
          renderDisplayBox(resultValueIndex, selectedIndex);
        }
      });
    }

    /**
     * Render the entire display.
     *
     * @param dataSets The set of data to be rendered.
     */
    function renderAll(dataSets){

      dataSetLink = dataSets;

      listBox.html("");
      var renderTargets = [];
      var listMaps = listBox.selectAll("svg")
        .data(dataSetLink[resultValueIndex])
        .enter()
        .append("svg")
        .attr("width","120")
        .attr("height","150")
        .attr("id", function(d, i) {
          var id = "list-map-" + i;
          renderTargets.push(id);
          return id;
        })
        .on('click', function(d, i) {
          selectedIndex = i;
          renderDisplayBox(resultValueIndex, selectedIndex);
        } );

      for(var i = 0; i < renderTargets.length; i++){

        d3Service.renderSingleHeatMap({
          location: "#" + renderTargets[i],
          data: dataSetLink[resultValueIndex][i].data,
          title: dataSetLink[resultValueIndex][i].name,
          mapFormat: {
            margin: 8,
            fixedsize_x: 90,
            fixedsize_y: 90
          }
        });

      }

      renderDisplayBox(resultValueIndex, selectedIndex);

    }

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
            var result = {
              colName: colName,
              rowName: rowName,
              data: d,
              name: "A" + String(Math.round(Math.random() * 400)),
              z: Math.round(Math.random() * 100) / 100,
              z_prime: Math.round(Math.random() * 100) / 100
            };
            result.name = result.name + " (z=" + result.z + ") (z'=" + result.z_prime + ")";
          return result;
          })
      })
    };


    // Result options.
    $scope.resultOptions = sampleJsonData.resultOptions.map(function(d,i){
      return {label: d, value: i};
    });
    $scope.result = $scope.resultOptions[0];

    // Watch for the
    $scope.$watch('result', function(newValue, oldValue) {
      resultValueIndex = newValue.value;
      renderAll(sampleJsonData.dataSets);
    });

  }]
);
