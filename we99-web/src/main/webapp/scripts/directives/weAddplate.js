'use strict';

/**
 * @ngdoc directive
 * @name we99App.directive:weAddplate
 * @description
 * # weAddplate
 */
angular.module('we99App')
    .directive('weAddplate', function () {
        return {
            restrict: 'E',
            //template:"<div>wire test</div>",
            templateUrl: 'views/plate-mgmt/addplate.html',
            scope: {},
            controller:'AddPlateCtrl'
            //link: function postLink(scope, element, attrs) {
            //  element.text('wire test');
            //}
        };
    })
    .controller('AddPlateCtrl', function($scope, $routeParams, kCompoundUOM, SelectedExperimentSvc, PlateTypeModel, PlateMapModel, LabelTableSvc) {
        $scope.selectedPlateType = null;
        $scope.selectedPlateMap = null;
        $scope.plateMaps = null;
        $scope.labelTable = [];
        $scope.UOMOptions = Object.keys(kCompoundUOM).map(function(key){
            return {key:key, value:kCompoundUOM[key]};
        });

        /** Grabs a list of plate types on load */
        $scope.plateTypes = PlateTypeModel.query(function done(data) {
            $scope.plateTypes = data;
            // DEFAULT VALUE
            $scope.selectedPlateType = data[0];
            $scope.plateMapsForPlateType();
        });

        /** Grabs a list of plate maps when a plate type is selected */
        $scope.plateMapsForPlateType = function () {
            var maxRows = $scope.selectedPlateType.dim.rows,
                maxCols = $scope.selectedPlateType.dim.cols;
            $scope.plateMaps = PlateMapModel.listPlateMaps({maxRows: maxRows, maxCols: maxCols},
                function done(data) {
                    $scope.plateMaps = data;
                    //// DEFAULT VALUE
                    //$scope.selectedPlateMap = data[0];
                    //$scope.makeLabelTable();
                });
        };

        $scope.computeReplicates = LabelTableSvc.computeReplicates;

        /** For Typeahead completion */
        $scope.findCompoundMatches = function(query) {
            return LabelTableSvc.findCompoundMatches(query)
                .then(function(data){
                    return data;
                });
        };

        /** Gets the merge info object from the server based on the plate map and type
         *  and uses it to populate the label table display.
         */
        $scope.getMergeInfo = function(){
            LabelTableSvc.retrieveMergeInfoTemplate($scope.selectedPlateMap.id, $scope.selectedPlateType)
                .then(function(mergeObject){
                    $scope.labelTable = mergeObject.mappings;
                    console.log($scope.labelTable);
                });
        };


        /** Submits a completed label table to produce the new plate sets for the experiment */
        $scope.submitPlateSet = function(){
            LabelTableSvc.submitMergeInfo($routeParams.experimentId, $scope.labelTable)
                .then(function(resp){
                    console.log('submitted!');
                    console.log(resp.data);
                    $scope.$parent.refreshPlates();
                });
        };
    });
