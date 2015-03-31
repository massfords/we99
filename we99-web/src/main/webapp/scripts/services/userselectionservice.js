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
    var selected,
        factory = {
        getSelected: function(){
          return selected;
        },
        setSelected: function(experiment){
          selected = experiment;
        },
        setSelectedById: function(experimentId) {
          RestService.getExperimentById(experimentId)
            .success(function(resp){
              factory.setSelected(resp);
            })
            .error(function(resp){
              console.error(resp);
              factory.clearSelection();
            });
        },
        clearSelection: function(){factory.setSelected(null);}
      };
    return factory;
  });
