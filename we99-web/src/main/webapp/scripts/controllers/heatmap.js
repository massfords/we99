'use strict';

function getDefaultPaginationInfo(data){
  var pageSize = 12;
  return {
    pageSize: pageSize,
    paginationIndex: 0,
    from: 1,
    to: Math.min(pageSize, data.length),
    of: data.length
  };
}

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

    // Stores the index of the presently selected value in the display box
    // relative to the dataset as a whole.
    $scope.selectedIndex = 1;



    // Links to display elements.
    $scope.renderTargets = {
      listBox:"#list-box",
      displayBox: "#display-box"
    };

    /**
     * Render the display box.
     *
     * @param data The data to be rendered in the display box.
     */
    function renderDisplayBox(){

      // Wipe out contents.
      d3.select($scope.renderTargets.displayBox).html("");

      // Render single largish heatmap.
      v.renderSingleHeatMap({
        location: $scope.renderTargets.displayBox,
        data: $scope.data[$scope.selectedIndex].data,
        colorScale:  $scope.coloring.colorScale,
        title: $scope.data[$scope.selectedIndex].name,
        onCellClick: function(d) {
          $scope.data[$scope.selectedIndex].data[d.wellIndex].included =
            !$scope.data[$scope.selectedIndex].data[d.wellIndex].included;
          renderAll($scope.data);
          renderDisplayBox();
        }
      });

      if(!$scope.$$phase) {
        $scope.$apply();
      }
    }


    /**
     * Render the entire display.
     *
     * @param dataSets The set of data to be rendered.
     */
    function renderAll(dataSets){

      var myData = dataSets.slice($scope.pagination.from, $scope.pagination.to + 1);

      // Relative index in the array.
      var relativeIndex = $scope.selectedIndex - ($scope.pagination.from);

      // Wipe out contents.
      var listBox = d3.select($scope.renderTargets.listBox).html("");

      var renderTargets = [];

      listBox.selectAll("svg")
        .data(myData)
        .enter()
        .append("svg")
        .attr("width","100")
        .attr("height","120")
        .attr("id", function(d, i) {
          var id = "list-map-" + i;
          renderTargets.push(id);
          return id;
        })
        .style("border", function(d, i) {
          if(i === relativeIndex){
            return "solid black 1px";
          }
        })
        .on('click', function(d, i) {
          $scope.selectedIndex = i + $scope.pagination.from;
          $scope.selectedIndex = i + $scope.pagination.from;
          renderAll($scope.data);
          renderDisplayBox();
        } );

      for(var i = 0; i < renderTargets.length; i++){

        v.renderSingleHeatMap({
          location: "#" + renderTargets[i],
          data: myData[i].data,
          title: myData[i].name,
          colorScale:  $scope.coloring.colorScale,
          mapFormat: {
            fixedsize_x: 65,
            fixedsize_y: 65
          }
        });


        if( i !== relativeIndex ) {
          v.addDarkOverlay(
            {location: "#" + renderTargets[i]}
          );
        }
      }

      v.heatMapColorGuide({
          location: "#gradients",
          colorScale: $scope.coloring.colorScale,
          min: $scope.coloring.min,
          max: $scope.coloring.max
        }

      );
    }

    var dataSet = [];
    for (var num = 0; num < 110; num++) {
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
              z_prime: Math.round(Math.random() * 100) / 100,
              pos_avg: Math.round(Math.random() * 100) / 100,
              neg_avg: Math.round(Math.random() * 100) / 100
            };
          return result;
      })
    };




    //retrieve list of experiments
    RestService.getExperiments()
      .success(function(response){

        // Data Set
        $scope.data = sampleJsonData.dataSets;


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

        var scale = calculateScale($scope.data);
        $scope.coloring.colorScale = scale.scale;
        $scope.coloring.min = scale.min;
        $scope.coloring.max = scale.max;

        function calculateScale(data){

          var values = [];

          data.forEach(function(dataSet) {
            dataSet.data.forEach(function (d) {
              values.push(d.value);
            })
          });

          return v.colorScale({
            data: values,
            colorOne: $scope.coloring.colorOption.colorOne,
            colorTwo: $scope.coloring.colorOption.colorTwo
          });

        }

        $scope.$watch('coloring.colorOption', function(newValue, oldValue){
          var scale = calculateScale($scope.data);
          $scope.coloring.colorScale = scale.scale;
          $scope.coloring.min = scale.min;
          $scope.coloring.max = scale.max;
          renderAll($scope.data);
          renderDisplayBox();
        });



        // Experiment Drop Down
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
            renderAll($scope.data);
            renderDisplayBox();
          });


        });

        // Pagination
        $scope.pagination = getDefaultPaginationInfo($scope.data);

        $scope.doPaging = function(direction) {
          switch(direction){
            case 'NEXT':
              console.log($scope.pagination);
              if( ( ($scope.pagination.pageSize * ($scope.pagination.paginationIndex + 1) )) < $scope.pagination.of){
                $scope.pagination.paginationIndex ++;
              }
              break;
            case "PREV":
              if($scope.pagination.paginationIndex > 0){
                $scope.pagination.paginationIndex --;
              }
              break;
          }
          $scope.pagination.from = ($scope.pagination.pageSize * $scope.pagination.paginationIndex) + 1;
          $scope.pagination.to = Math.min( ($scope.pagination.from - 1) + $scope.pagination.pageSize, $scope.data.length)
          renderAll($scope.data);
        }


      })
      .error(function(response){
        $scope.errorText="Could not retrieve experiments list.";
      });



  }]
);
