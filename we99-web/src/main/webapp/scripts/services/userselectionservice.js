'use strict';

/**
 * @ngdoc service
 * @name we99App.SelectedExperimentSvc
 * @description
 * # SelectedExperimentSvc
 * Stored the experiment object selected by the user
 */
angular.module('we99App')
  .factory('SelectedExperimentSvc', function (RestService) {
    var selected;
    return {
      selected: function(){return selected;},
      setSelectedById: function(experimentId) {
        RestService.getExperimentById(experimentId)
          .success(function(resp){
            selected=resp;
          })
          .error(function(resp){
            console.error(resp);
            selected = null;
          });
      }
    };
  });
