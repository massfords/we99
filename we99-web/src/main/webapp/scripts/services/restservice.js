'use strict';

/**
 * @ngdoc service
 * @name we99App.restService
 * @description
 * # restService
 * Service in the we99App.
 */
var app = angular.module('we99App');
app.factory('RestService', ['$resource','$http','RestURLs', function ($resource,$http, RestURLs) {
    return {


    	//
    	// $resource-style calls
    	//

    	plateType: $resource(RestURLs.plateType,{}, {
            query: {method: "GET",
                    isArray: true,
                    // Get an array back to exhibit expected query behavior
                    transformResponse: valuesToArray
            },
            put: {method: "PUT", isArray: false}
        }),
        //experiment: $resource(RestURLs.experiment,{},{}),
        results: $resource(RestURLs.result,{}, {}),

        //
        // $http style calls.
        //

        //experiments
        getExperiments:  function(){
        	return $http.get(RestURLs.experiment);
        },
        createExperiment: function(exp){
        	return $http.put(RestURLs.experiment,exp);
        },
        saveExperiment: function(exp){
        	return $http.post(RestURLs.experiment+'/'+exp.id,exp);
        },
        deleteExperiment: function(id){
        	return $http.delete(RestURLs.experiment+'/'+id);
        },
        
        getExperimentPlates: function(id){
     	 return $http.get(RestURLs.experiment + '/' + id + "/plates")
    },

        //users
        getUsers:  function(){
        	return $http.get(RestURLs.user);
        },

        //protocol
        getProtocols:  function(){
        	return $http.get(RestURLs.protocol);
        },
        
        //email filter
        getEmailFilter: function(){
        	return $http.get(RestURLs.emailFilter);
        },
        
        updateEmailFilter: function(){
        	return $http.post(RestURLs.emailFilter);
        },
        //server settings
        getServerSettings: function(){
        	return $http.get(RestURLs.serverSettings);
        },
        updateServerSettings: function(){
        	return $http.post(RestURLs.serverSettings);
        }
    };
}]);

/** REST linked resource model for Plate Type */
app.factory('PlateTypeModel', ['$resource', 'RestURLs', function ($resource, RestURLs) {
  return $resource(RestURLs.plateType, {id:'@id'}, {
    query: {
      method: "GET",
      isArray: true,
      // Get an array back to exhibit expected query behavior
      transformResponse: valuesToArray
    },
    create: {
      method: "PUT" // Server takes put for creation
    }
  });
}]);


/* HELPER FUNCTIONS */

function valuesToArray(data) {
  if (data) {
    return JSON.parse(data).values;
  } else {
    return null;
  }
}
