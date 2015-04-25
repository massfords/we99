'use strict';

angular.module('we99App')
  .controller('AddPlateCtrl', function ($scope, $routeParams, $modalInstance, kCompoundUOM, MergeType,
                                        SelectedExperimentSvc, PlateTypeModel, PlateMapModel,
                                        LabelTableSvc, PlateMergeRestService) {
    $scope.MergeType = MergeType;
    $scope.selectedPlateType = null;
    $scope.selectedPlateMap = null;
    $scope.csvFiles = null; // angular-file-upload treats model as a list even if only accepts one.
    $scope.plateMaps = null;
    $scope.labelTable = [];
    $scope.UOMOptions = Object.keys(kCompoundUOM).map(function (key) {
      return {key: key, value: kCompoundUOM[key]};
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
    $scope.findCompoundMatches = function (query) {
      return LabelTableSvc.findCompoundMatches(query)
        .then(function (data) {
          return data;
        });
    };

    /** Determines whether or not to activate the create button in the add plate screen
     *
     * @param mergeType add plate type can be "ADD", "ADD_W_CMPD", "FULL_MONTY"
     * @returns {boolean}
     */
    $scope.showCreateButton = function (mergeType) {

      switch (mergeType) {
        case MergeType.ADD:
          return $scope.labelTable.length > 0 &&
            $scope.labelTable.every(function (row) {
              return (row.dose.compound && row.dose.compound.length > 0);
            });
        case MergeType.ADD_W_CMPD:
          return $scope.labelTable.length > 0 &&
            $scope.csvFiles &&
            $scope.csvFiles.length === 1;
        case MergeType.FULL_MONTY:
          return !!$scope.selectedPlateType &&
            $scope.csvFiles &&
            $scope.csvFiles.length === 1;
        default:
          console.error("Need to set parameters for merge type: " + mergeType);
          return false;
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss();
    };

    /** Gets the merge info object from the server based on the plate map and type
     *  and uses it to populate the label table display.
     */
    $scope.getMergeInfo = function () {
      LabelTableSvc.retrieveMergeInfoTemplate($scope.selectedPlateMap.id, $scope.selectedPlateType)
        .then(function (mergeObject) {
          $scope.labelTable = mergeObject.mappings;
          console.log($scope.labelTable);
        });
    };


    /** For Addplate Popup:
     * Submits a completed label table to produce the new plate sets for the experiment
     */
    $scope.submitPlateSet = function () {
      LabelTableSvc.submitMergeInfo($routeParams.experimentId, $scope.plateName, $scope.labelTable)
        .then(function (resp) {
          console.log('submitted!');
          console.log(resp.data);
          $modalInstance.close();
        });
    };

    $scope.submitPlateSetWithCompoundList = function () {
      var csvFile = $scope.csvFiles[0];
      LabelTableSvc.submitMergeInfo($routeParams.experimentId, $scope.plateName, $scope.labelTable, csvFile)
        .then(function (resp) {
          console.log('submitted!');
          console.log(resp.data);
          $modalInstance.close();
        });
    };

    $scope.submitPlateSetFullMonty = function () {
      if (!$scope.selectedPlateType || !$scope.csvFiles || !$scope.csvFiles[0]) {
        console.error("A Parameter is missing.");
        console.error("SelectedPlateType: " + $scope.selectedPlateType + "\nCsvFiles: " + JSON.stringify($scope.csvFiles));
      }
      PlateMergeRestService.submitPlatesWithResults($routeParams.experimentId, $scope.selectedPlateType, $scope.csvFiles)
        .then(function (resp) {
          console.log('submitted!');
          console.log(resp.data);
          $modalInstance.close();
        });
    };


  });
