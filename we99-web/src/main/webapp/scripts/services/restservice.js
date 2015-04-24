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


    // OK TO Delete?
    ////
    //// $resource-style calls
    ////
    //
    //plateType: $resource(RestURLs.plateType,{}, {
    //  query: {method: "GET",
    //    isArray: true,
    //    // Get an array back to exhibit expected query behavior
    //    transformResponse: valuesToArray
    //  },
    //  put: {method: "PUT", isArray: false}
    //}),
    ////experiment: $resource(RestURLs.experiment,{},{}),
    //results: $resource(RestURLs.result,{}, {}),

    //
    // $http style calls.
    //

    //experiments
    getExperiments:  function(){
      return $http.get(RestURLs.experiment);
    },
    getExperimentById:  function(id){
      return $http.get(RestURLs.experiment+'/'+id);
    },
    createExperiment: function(exp){
      if(exp.id)
        return $http.post(RestURLs.experiment+'/'+exp.id,exp);
      else
        return $http.put(RestURLs.experiment,exp);
    },
    publishExperiment: function(expId){
      return $http.post(RestURLs.experiment + '/' + expId + '/publish');
    },
    deleteExperiment: function(id){
      return $http.delete(RestURLs.experiment+'/'+id);
    },
    setExperimentMembers: function(expId,memberIds){
      return $http.post(RestURLs.experiment+'/'+expId+'/members',memberIds);
    },
    getExperimentMembers: function(expId){
      return $http.get(RestURLs.experiment+'/'+expId+'/members');
    },

    // Plates
    getExperimentPlates: function(id){
      return $http.get(RestURLs.experiment + '/' + id + "/plates")
    },
    removeExperimentPlate: function(experimentId, plateId) {
      return $http.delete(RestURLs.experiment + '/' + experimentId  + "/plates/" + plateId);
    },
    // Plate results
    getPlateResults: function(experimentId,plateId){
      return $http.get(RestURLs.experiment+'/'+experimentId+'/plates/'+plateId+'/results');
    },
    //email filter
    getEmailFilter: function(){
      return $http.get(RestURLs.emailFilter);
    },

    updateEmailFilter: function(filter){
      return $http.post(RestURLs.emailFilter,filter);
    },
    //server settings
    getServerSettings: function(){
      return $http.get(RestURLs.serverSettings);
    },
    updateServerSettings: function(emailConfig){
      return $http.post(RestURLs.serverSettings,emailConfig);
    },

    //users
    getUsers:  function(){
      return $http.get(RestURLs.user);
    },

    //returns currently logged-in User
    getCurrentUser:  function(){
      return $http.get(RestURLs.user+'/me');
    },

    //protocol
    getProtocols:  function(){
      return $http.get(RestURLs.protocol);
    },

    addProtocol:  function(proc){
      return $http.put(RestURLs.protocol,proc);
    },

    //compound
    getCompounds: function(searchFilter,pageSize){
      var config={params:{"pageSize": pageSize, page:0, q: searchFilter}};
      return $http.get(RestURLs.compoundUpload,config);
    }

  };

}]);


app.factory('PlateMergeRestService', ['$http', '$q', '$upload', 'RestURLs', function($http,$q,$upload,RestURLs){
  return {
    // For Label Table Gets a partially completed platemap merge info object
    getMergeInfoTemplate: function (plateMapId, plateType) {
      return $http.post(replaceId(RestURLs.mergeInfoTemplate, plateMapId), plateType);
    },
    // Submit to make plate from user parameters
    submitMergeInfo: function (experimentId, mergeInfoObject) {
      //if (!mergeInfoObject.plateName) {mergeInfoObject.plateName = 'Plate-TS' + Date.now();}
      return $http.put(replaceId(RestURLs.mergeInfoSubmit, experimentId), mergeInfoObject);
    },
    // Make plates from Full Monty Csv.
    submitPlatesWithResults: function (experimentId, plateType, csvFiles) {
      if (!csvFiles || csvFiles.length !== 1) {
        console.error('No csv file attached');
        return null;
      }
      var file = csvFiles[0],
        upload = $upload.upload({
          url: RestURLs.experiment + "/" + experimentId + "/fullMonty",
          method: "POST",
          fields: {
            plateType: plateType
          },
          file: file
        }).progress(function (event) {
          var progressPercentage = parseInt(100.0 * event.loaded / event.total);
          console.log('progress: ' + progressPercentage + '% ' +
          event.config.file.name);
        }).success(function (data, status, headers, config) {
          var result = JSON.stringify(data);
          console.log('file ' + config.file.name + 'uploaded. Response: ' +
          result);
        }).error(function (data, status, headers, config) {
          var result = JSON.stringify(data);
          console.log('error in file ' + config.file.name + 'uploaded. Response: ' +
          result);
        });
      return $q.when(upload);
    }
  };

  function replaceId(urlString, id) {
    return urlString.replace(':id', id);
  }
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

/** REST linked resource model for Plate Maps */
app.factory('PlateMapModel', ['$resource', 'RestURLs', function ($resource, RestURLs) {
  return $resource(RestURLs.plateMap, {id:'@id'}, {
    listPlateMaps: {
      method: "GET",
      isArray: true,
      // Get an array back to exhibit expected query behavior
      transformResponse: valuesToArray
    },
    create: {
      method: "PUT" // Server takes put for creation
    }
    //// Requires a plateType in the body. remember the '$' before using because its an instance call.
    //getMergeInfo: {
    //  method:"POST",
    //  url: RestURLs.mergeInfoTemplate
    //}
  });
}]);

/** REST linked resource model for Compounds */
app.factory('CompoundModel', ['$resource', 'RestURLs', function($resource, RestURLs){
  var TYPEAHEAD_RESULT_SIZE = 4;
  return $resource(RestURLs.compound, {id: '@id'}, {
    list: {
      isArray:true,
      transformResponse: valuesToArray
    },
    // When using get typeahead, make sure to put in a parameter value for 'q'
    getTypeAhead: {
      // only grab first 4 results
      params: {q:'', pageSize:TYPEAHEAD_RESULT_SIZE},
      cache: true,
      isArray:true,
      transformResponse: valuesToArray
    },
    create: {
      method:"PUT"
    }
  }); // close $resource
}]);

/* HELPER FUNCTIONS */

/** Function to get the array of response values in 'list' style calls.
 *  needed because list style calls are returned as objects with the value information provided along with metadata
 */
function valuesToArray(data) {
  if (data) {
    return JSON.parse(data).values;
  } else {
    return null;
  }
}
