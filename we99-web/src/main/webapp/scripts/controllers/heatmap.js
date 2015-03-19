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
  .controller('HeatmapCtrl', ["$scope", "RestService", function ($scope, RestService) {

    var v = new DataVis();

    // Stores the index of the presently selected value in the display box.
    var selectedIndex = 0;

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
    function renderDisplayBox(){
      displayBox.html("");
      v.renderSingleHeatMap({
        location: "#display-box",
        data: dataSetLink[selectedIndex].data,
        title: dataSetLink[selectedIndex].name,
        onCellClick: function(d) {
          dataSetLink[selectedIndex].data[d.wellIndex].included =
            !dataSetLink[selectedIndex].data[d.wellIndex].included;
          renderDisplayBox();
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

      listBox.selectAll("svg")
        .data(dataSetLink)
        .enter()
        .append("svg")
        .attr("width","75")
        .attr("height","75")
        .attr("id", function(d, i) {
          var id = "list-map-" + i;
          renderTargets.push(id);
          return id;
        })
        .style("border", function(d, i) {
          if(i=== selectedIndex){
            return "solid black 1px";
          }
        })
        .on('click', function(d, i) {
          selectedIndex = i;
          renderAll(dataSetLink);
        } );

      for(var i = 0; i < renderTargets.length; i++){

        v.renderSingleHeatMap({
          location: "#" + renderTargets[i],
          data: dataSetLink[i].data,
          title: dataSetLink[i].name,
          mapFormat: {
            fixedsize_x: 65,
            fixedsize_y: 65
          }
        });

        if(i != selectedIndex) {
          v.addDarkOverlay(
            {location: "#" + renderTargets[i]}
          );
        }
      }
      renderDisplayBox(selectedIndex);
    }


    var dataSet = [];
    for (var num = 0; num < 15; num++) {
      var array = [];
      var index = 0;
      for (var i = 0; i < 9; i++) {
        for (var j = 0; j < 9; j++) {

          array.push({
            wellIndex: index++,
            row: i,
            col: j,
            value: Math.random(),
            included: true,
            wellType: "NORMAL"
          });

          if(Math.random() <= 0.05){
            array[array.length - 1].wellType = "CONTROL";
          }

        }
      }
      dataSet[num] = array;
    };

    var sampleJsonData = {
      dataSets: dataSet.map(function (d) {
            var result = {
              data: d,
              name: "A" + String(Math.round(Math.random() * 400)),
              z: Math.round(Math.random() * 100) / 100,
              z_prime: Math.round(Math.random() * 100) / 100
            };
            result.name = result.name + " (z=" + result.z + ") (z'=" + result.z_prime + ")";
          return result;
      })
    };



    //retrieve list of experiments
    RestService.getExperiments()
      .success(function(response){

        $scope.experiments=response.values;
        $scope.selectedExperiment = $scope.experiments[0];

        $scope.$watch('selectedExperiment', function(newValue, oldValue){

          console.log(newValue);
          RestService.getExperimentPlates(newValue.id)
            .success(function(response){
              var arr = response.values;
              console.log(arr);
            }).error(function(response){
              $scope.errorText="Could not retrieve experiments list.";
            });

          $scope.$watch('selectedExperiment', function(newValue, oldValue) {
            renderAll(sampleJsonData.dataSets);
          });


        });

      })
      .error(function(response){
        $scope.errorText="Could not retrieve experiments list.";
      });



  }]
);
