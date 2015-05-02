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
  .controller('HeatmapCtrl', ["$q", "$scope", "RestService","TourConstants", function ($q, $scope, RestService,TourConstants) {

    var v = new DataVis();

    // Links to display elements.
    var listBoxLocation = "#list-box";
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


        $scope.selectPlate = function (plateId){
          var counter = 0;
          $scope.data.forEach(function (d){
            counter = counter + 1;
            if(d.plateIndex === plateId){
              $scope.selectedIndex = counter;
            }
          });
          renderListView($scope.data);
          renderSingleView();
        };

        $scope.$watch('selectedExperiment', function(newValue, oldValue){

          var experimentId = newValue.id;

          RestService.getExperimentPlates(experimentId)
            .success(function(response){
              var plateIds = response.values.filter(function(plate){ return plate.hasResults;}).map(function(plate){return plate.id;});
              var promises = plateIds.map(function(plateId){ return RestService.getPlateResults(experimentId, plateId); });
              $q.all(promises).then(function(response){

                $scope.data = v.convertPlateResultData(response.map(function(d){return d.data;}));

                // Stores the index of the presently selected value in the display box
                // relative to the dataset as a whole.
                $scope.selectedIndex = 1;

                // Coloration change watcher.
                $scope.$watch('coloring.colorOption', function(newValue, oldValue){ fullDisplayRefresh() });

                // Pagination
                $scope.pagination = getDefaultPaginationInfo($scope.data);
                fullDisplayRefresh();


              });
            }).error(function(response){
              $scope.errorText="Could not retrieve plate list for expriement [id=" + experimentId + "]";
            });;

        });

      })
      .error(function(response){
        $scope.errorText="Could not retrieve experiments list.";
      });

    /**
     * ###################################
     * Helper Functions
     * ###################################
     */

    function fullDisplayRefresh(){
      setUpNewScale();
      renderListView($scope.data);
      renderSingleView();
    }

    /**
     * Upacks the scope values
     */
    function setUpNewScale(){

      var values = [];
      $scope.data.forEach(function(dataSet) {
        dataSet.data.forEach(function (d) {
          values.push(d.value);
        })
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

    /**
     * Render the display box.
     *
     * @param data The data to be rendered in the display box.
     */
    function renderSingleView(){

      function toText(bool){
        if(bool){
          return "INCLUDED";
        }else{
          return "EXCLUDED";
        }
      }

      // Wipe out contents.
      d3.select(displayBoxLocation).html("");

      // Render single largish heatmap.
      v.renderSingleHeatMap({
        location: displayBoxLocation,
        data: $scope.data[$scope.selectedIndex - 1].data,
        colorScale:  $scope.coloring.colorScale,
        mapFormat: {
          fixedsize_x: 400,
          fixedsize_y: 400
        },
        onCellClick: function(d) {
          $scope.data[$scope.selectedIndex - 1].data.forEach(function(dinner){
            if(dinner.wellIndex === d.wellIndex){
              dinner.included = !dinner.included;

              RestService.updatePlateResults(
                  $scope.data[$scope.selectedIndex - 1].experimentIndex,
                  $scope.data[$scope.selectedIndex - 1].plateIndex, {
                    status: toText(dinner.included),
                    coordinate:{
                      col: dinner.col,
                      row: dinner.row
                    }
                  }
                ).success(function(response){
                  $scope.data[$scope.selectedIndex - 1] =  v.convertPlateResultData([response])[0];
                  console.log($scope.data);
                  fullDisplayRefresh();
                });

            }
          });
          renderListView($scope.data);
          renderSingleView();
        }
      });

      // Re-render the details of the plate being displayed -- managed by angular.
      if(!$scope.$$phase) {
        $scope.$apply();
      }
    }

    /**
     * Render the list view display.
     *
     * @param dataSets The set of data to be rendered.
     */
    function renderListView(dataSets){

      var myData = dataSets.slice($scope.pagination.from - 1, $scope.pagination.to);

      // Relative index in the array.
      var relativeIndex = $scope.selectedIndex - ($scope.pagination.from);

      // Wipe out contents.
      var listBox = d3.select(listBoxLocation).html("");

      // Create a place to render each of the heat maps.
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
          renderListView($scope.data);
          renderSingleView();
        } );

      // Render each heatmap in the new location.
      for(var i = 0; i < renderTargets.length; i++){

        v.renderSingleHeatMap({
          location: "#" + renderTargets[i],
          data: myData[i].data,
          title: myData[i].name,
          colorScale:  $scope.coloring.colorScale,
          mapFormat: {
            fixedsize_x: 113,
            fixedsize_y: 113
          }
        });


        // Add 'grey' unselected box if its not selected.
        if( i !== relativeIndex ) {
          v.addDarkOverlay(
            {location: "#" + renderTargets[i]}
          );
        }
      }

      // Create a color guide for the heat map.
      v.heatMapColorGuide({
          location: "#gradients",
          colorScale: $scope.coloring.colorScale,
          min: $scope.coloring.min,
          max: $scope.coloring.max
        }

      );
    }


    /**
     * Return default pagination information.
     *
     * @param data
     * @returns {{pageSize: number, paginationIndex: number, from: number, to: number, of: *}}
     */
    function getDefaultPaginationInfo(data){
      var pageSize = 12;
      return {
        pageSize: pageSize,
        paginationIndex: 0,
        from: 1,
        to: Math.min(pageSize, data.length),
        of: data.length,
        doPaging: function(direction) {
          switch(direction){
            case 'NEXT':
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
          renderListView($scope.data);
        }
      };
    }

    //=== Tour Settings ===

    $scope.startTour=function(){
      $scope.tourConfig=TourConstants.cleanTourConfig(TourConstants.heatmapTour);
      if($scope.tourConfig.length>=1)
        $scope.startJoyRide=true;
    };


  }]
);
