'use strict';

/**
 * @ngdoc service
 * @name we99App.SelectedExperimentSvc
 * @description
 * # SelectedExperimentSvc
 * Stored the experiment object selected by the user
 */
angular.module('we99App')
    .factory('SelectedExperimentSvc', function ($q, RestService) {
        var selected,
            factory = {
                getSelected: function(){
                    return selected;
                },
                setSelected: function(experiment){
                    selected = experiment;
                },
                setSelectedById: function(experimentId) {
                    var deferred = $q.defer();
                    RestService.getExperimentById(experimentId)
                        .success(function(resp){
                            factory.setSelected(resp);
                            deferred.resolve(selected);
                        })
                        .error(function(err){
                            console.error(err);
                            factory.clearSelection();
                            deferred.reject(err);
                        });
                    return deferred.promise;
                },
                clearSelection: function(){factory.setSelected(null);},
                getPlates: function(){
                  var deferred = $q.defer();
                  if (!selected) {
                    throw Error('No Experiment has been selected.');
                  }
                  RestService.getExperimentPlates(selected.id).then(function(resp){
                    var plates = resp.data.values;
                    deferred.resolve(plates);
                  }, function(err) {
                    deferred.reject(err);
                  });
                  return deferred.promise;
                }
            };
        return factory;
    });
